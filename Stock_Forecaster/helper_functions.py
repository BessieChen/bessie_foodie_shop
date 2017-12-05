# Set of helper functions for main algorithm

import numpy as np
import quandl
import pandas as pd
import talib

import datetime

# Adopted from: https://stackoverflow.com/questions/29721228/given-a-date-range-how-can-we-break-it-up-into-n-contiguous-sub-intervals
def date_range(start, end, prop, set_time = "None"):
    """
    Splits the interval [start, end] into 'prop' train and '1-prop' test.
    :param start: start date
    :param end: end date
    :param prop: proportion of [start, end] time interval for training
    :param set_time: explicit date to split training/testing data (if input)
    :return: date which splits [start, end] into 'prop' train and '1-prop' test
    """
    if set_time == "None":
        from datetime import datetime
        start = datetime.strptime(start,"%b %d %Y")
        end = datetime.strptime(end,"%b %d %Y")
        diff = (end  - start ) * prop
        yield (start + diff).strftime("%b %d %Y")

    # If specific split time is included, return the specific time
    else:
        yield set_time

def get_curr_date():
    """
    Returns current date formatted with month as a string
    :return: current date formatted as 'Month Day Year' where month is the month as a string of length 3
    """
    # Gets current date as month-day-year
    now = datetime.datetime.now()
    now = now.strftime('%m %d %Y')

    # Reformats date with month as a string
    time_arr = now.split(" ")
    month = datetime.date(1900, int(time_arr[0]), 1).strftime('%b')
    time_arr[0] = month
    curr_time = " ".join(time_arr)

    return curr_time

def read_txt(fname):
    """
    Reads user input from the parameters text file.
    :param fname: text file
    :return: array of stocks and the forecast period
    """
    # Opens the file
    with open(fname) as f:
        content = f.readlines()

    # Reads the lines of the file and removes empty lines
    content = [line.strip() for line in content]
    content = [s for s in content if s != ""]

    # Finds index of the "Ticker" input and gets the stock array
    matching = [s for s in content if "Ticker" in s]
    tickers_index = content.index(matching[0])
    stocks = [ticker.strip() for ticker in content[tickers_index+1].split(',')]

    # Finds the index of the "Forecast Period" input and gets the forecast period
    matching = [s for s in content if "Forecast Period" in s]
    pred_period = content.index(matching[0])
    period = content[pred_period+1]

    return stocks, int(period)

def exponential_smoothing(alpha, input_data):
    """
    Performs exponential smoothing. This weighs more recent price data higher than past data.
    :param alpha: weight of current data ('1' keeps data unchanged)
    :param input_data: data to smooth
    """
    # Exponentially smooths input prices beginning from the most recent
    for i in reversed(range(0, len(input_data)-1)):
        input_data.iloc[i] = input_data.iloc[i+1]*(1-alpha) + alpha * input_data.iloc[i]

def create_shifted_orderbook(ticker, start_date, end_date, lag_period = 5, pred_period = 7):
    """
    Shifts order book data. Determines stock movement direction. Incorporates technical indicators as training features.
    :param ticker: stock ticker
    :param start_date: start date for stock data collection period
    :param end_date: end date for stock data collection period
    :param lag_period: number of previous prices trained on
    :param pred_period: number of days forecast
    :return:
            stock_data: the price/volume info for a stock
            stock_returns: percent change between days and lagging percent changes (i.e. previous days' changes)
            stock_lag: stock price and lagging stock price
            stock_movement: stock movement direction (+1: increase, -1: decrease)
    """
    # Gets stock data from quandl
    quandl.ApiConfig.api_key = "97-z53C6RuRL7yZN1PYW"
    stock_data = quandl.get("WIKI/" + ticker, start_date=start_date, end_date=end_date)

    # Remove NaNs from stock data
    stock_data.dropna()

    # Creates stock lag and return data frames
    stock_lag = pd.DataFrame(index=stock_data.index)
    stock_returns = pd.DataFrame(index=stock_data.index)

    # Initializes dataframe values and smooths the closing price data
    stock_data_smooth = stock_data['Adj. Close']
    exponential_smoothing(0.7, stock_data_smooth)

    stock_lag['Volume'] = stock_returns['Volume'] = stock_data['Volume']
    close = stock_lag['Price'] = stock_data_smooth
    high = stock_data['Adj. High']
    low = stock_data['Adj. Low']
    volume = stock_data['Adj. Volume']

    # Sets lagging price data (previous days' price data as feature inputs)
    for i in range(0, lag_period):
        column_label = 'Lag{:d}'.format(i)
        stock_lag[column_label] = stock_lag['Price'].shift(1+i)

    # Adds additional features using technical indicators
    close = close.values
    high = high.values
    low = low.values
    volume = volume.values

    # EMA- Momentum
    stock_lag['EMA'] = talib.EMA(close, timeperiod = 30)
    # Bollinger Bands
    stock_lag['upperband'], stock_lag['middleband'], stock_lag['lowerband'] = talib.BBANDS(close, timeperiod=5, nbdevup=2, nbdevdn=2, matype=0)
    # StochK
    stock_lag['slowk'], stock_lag['slowd'] = talib.STOCH(high, low, close, fastk_period=14, slowk_period=3, slowk_matype=0, slowd_period=3,
                         slowd_matype=0)
    # MACD- Momentum
    macd, macdsignal, stock_lag['macdhist'] = talib.MACD(close, fastperiod=12, slowperiod=26, signalperiod=9)
    # CCI- Momentum
    stock_lag['CCI'] = talib.CCI(high, low, close)
    # RSI- Momentum
    stock_lag['RSI'] = talib.RSI(close, timeperiod=14)
    # William's R- Momentum
    stock_lag['WILLR'] = talib.WILLR(high, low, close, timeperiod=14)
    # Chaikin- Volume
    stock_lag['Chaikin'] = talib.ADOSC(high, low, close, volume, fastperiod=3, slowperiod=10)

    # Calculates percent change between days
    stock_returns['Day Returns'] = stock_data['Adj. Close'].pct_change() * 100

    # Sets lagging percent change data
    for i in range(0, lag_period):
        column_label = 'Lag{:d}'.format(i)
        stock_returns[column_label] = stock_lag[column_label].pct_change() * 100

    # Remove NaN's from stock lag
    stock_lag = stock_lag.dropna()

    # Adjusts stock_return data to same length as stock_lag
    stock_returns = stock_returns.tail(stock_lag.shape[0])

    # Determine stock movement direction and lagging movement
    stock_movement = pd.DataFrame(index=stock_returns.index)
    stock_movement['Movement_0'] = np.sign(stock_returns['Day Returns'])
    stock_movement['Movement_0'][0] = 1
    for i in range(0, pred_period):
        column_label = 'Movement_{:d}'.format(i+1)
        stock_movement[column_label] = stock_movement['Movement_0'].shift(i+1)

    # Removes NaNs from 'stock_movement' and resizes 'stocks_returns' and 'stock_lag' accordingly
    stock_movement = stock_movement.dropna()
    stock_returns = stock_returns[stock_returns.index <= stock_movement.index[stock_movement.index.__len__()-1]]
    stock_returns = stock_returns.tail(stock_movement.shape[0])
    stock_lag = stock_lag[stock_lag.index <= stock_movement.index[stock_movement.index.__len__() - 1]]
    stock_lag = stock_lag.tail(stock_movement.shape[0])

    return stock_data, stock_returns, stock_lag, stock_movement
