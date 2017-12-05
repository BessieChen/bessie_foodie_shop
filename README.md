# Stock Forecasting with a Random Forest Classifier
Stock price forecasting using a random forest (RF) classifier

## Overview
This program is a stock price forecasting algorithm. It uses a RF classifier that is trained and tested using stock price and volume data ranging from a given start date to end date. Training features include stock price data and multiple technical indicators. The stock price forecast period can be customized. The algorithm takes an array of stock tickers as input. Each ticker in the array is trained and tested independently.

For each ticker, the algorithm outputs a graph of the average prediction accuracy of the ticker's test set over the test period and for the desired number of forecast days.

The following technical indicators were used as input features: 
* Volume, Exponential Moving Average (EMA), Bollinger Bands, MACD, Commodity Channel Index (CCI), Relative Strength Index (RSI), Williams %R, Chaikin Oscillator, Slow Stochastic Oscillator

20 stocks from the S&P 500 were used as a baseline for testing algorithm accuracy. For this test, the following parameters were used:
* Start-Date: Jan. 1, 2000
* End-Date: Dec. 1, 2017
* Training Prop: 0.8
* Lag (i.e. Past Price Data Used): 20
* Prediction Period: 7
* Cross-Validation Splits: 3

One-Split Accuracy

<img src="https://github.com/Bryanlee99/Stock_Forecasting_RF/blob/master/Images/SP500_10_Stock_Test.PNG" width="600">

| Forecast Day | Day 1  | Day 2 | Day 3  | Day 4 |  Day 5 | Day 6 |  Day 7 |   
| -------------| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | 
| Avg. Accuracy | 0.71705267 | 0.68505514 | 0.66440167 | 0.65691662 | 0.65328077 | 0.63445114 | 0.623557 |

| Forecast Day | Day 1  | Day 2 | Day 3  | Day 4 |  Day 5 | Day 6 |  Day 7 |   
| -------------| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | 
| Avg. Accuracy | 0.7118233 | 0.68891018 | 0.66995239 | 0.6559209 | 0.63126462 | 0.61249649 | 0.60380727 |
## Usage
1. Open *parameters.txt* and input the listed parameters (of the format "[Parameter-name]: [value]"). Proper parameter format is included in the text file. 
2. Run *predict.py*

## Functionality
Additional notes on functionality are included in the code comments and the documentation included with each function. 

### parameters.txt
This file takes user input to select the stocks to be forecasted and the future forecast period (the number of days the stock movement will be predicted). These inputs are recorded in the file under the 'Stocks to Forecast' and 'Forecast Period' lines, respectively.

### predict.py
This is the main file of the program. The function iterates through each input ticker and, for each, prepares training/testing data, trains the RF model, and stores the accuracy results for each run. The RF models are trained and tested using both one-split and cross-validation on the data for each ticker. The results of both are then output as plots.

### helper_functions.py
This file implements multiple functions which are used by *predict.py*. 

The most extensive function is *create_shifted_orderbook*. This function retrieves the stock data (using the Quandl library) and pre-processes it. The function then adds previous stock price columns to the training data frame and calculates multiple technical indicators (using the Ta-Lib library) and adds them as input features. Lastly, it calculates the percent returns for each day and uses this to determine the direction of stock movement as positive (1) or negative (-1). Additionally, all dataframes are resized to the same shape for training and testing.

### fit\_model_functions.py
This file implements the one-split and cross-validation training and testing algorithms for the RF model. Previous price data and technical indicators are input features while the direction of price movement is the output. After training the model, the function returns the testing prediction accuracy of the trained model.

## Additional Information
### Requirements
* Python 3.5
* Matplotlib
* Numpy
* Sklearn
* Pandas
* Quandl
* Ta-Lib
