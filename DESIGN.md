# Algorithm Design
Description of key design decisions and aspects of algorithm function.
## Results Overview
10 stocks from the S&P 500 were used as a baseline for testing algorithm accuracy. For this test, the following parameters were used:
* Start-Date: Jan. 1, 2000
* End-Date: Dec. 1, 2017
* Training Prop: 0.8
* Lag (i.e. Past Price Data Used): 20
* Prediction Period: 7
* Cross-Validation Splits: 3

A graph of the one-split accuracy, and two tables of the average one-split accuracy and the average of the cross-validation tests are displayed below (in that order).

<img src="https://github.com/Bryanlee99/Stock_Forecasting_RF/blob/master/Images/SP500_10_Stock_Test.PNG" width="600">

| Forecast Day | Day 1  | Day 2 | Day 3  | Day 4 |  Day 5 | Day 6 |  Day 7 |   
| -------------| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | 
| Avg. Accuracy | 0.71705267 | 0.68505514 | 0.66440167 | 0.65691662 | 0.65328077 | 0.63445114 | 0.623557 |

| Forecast Day | Day 1  | Day 2 | Day 3  | Day 4 |  Day 5 | Day 6 |  Day 7 |   
| -------------| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | 
| Avg. Accuracy | 0.7118233 | 0.68891018 | 0.66995239 | 0.6559209 | 0.63126462 | 0.61249649 | 0.60380727 |

## Functionality
Additional notes on functionality are included in the code comments and the documentation included with each function. The general order of usage of functions from each file is as follows: *predict.py* calls various helper functions from *helper_functions.py* throughout, and at the end trains/tests using functions from *fit_model_functions.py* and runs backtesting through *backtest.py*.
### requirements.txt
This file includes all the necessary Python libraries to run the code.

### parameters.txt
This file takes user input to select the stocks to be forecasted and the future forecast period (the number of days the stock movement will be predicted). These inputs are recorded in the file under the 'Stocks to Forecast' and 'Forecast Period' lines, respectively.

### sample_inputs.txt
This file includes sample inputs that can be copied to *parameters.txt* for usage.

### predict.py
This is the main file of the program. The function iterates through each input ticker and, for each, prepares training/testing data, trains the RF model, and stores the accuracy results for each run. The RF models are trained and tested using both one-split and cross-validation on the data for each ticker. The results of both are then output as plots.

### helper_functions.py
This file implements multiple functions which are used by *predict.py*. 

The most extensive function is *create_shifted_orderbook*. This function retrieves the stock data (using the Quandl library) and pre-processes it. The function then adds previous stock price columns to the training data frame and calculates multiple technical indicators (using the Ta-Lib library) and adds them as input features. Lastly, it calculates the percent returns for each day and uses this to determine the direction of stock movement as positive (1) or negative (-1). Additionally, all dataframes are resized to the same shape for training and testing.

### fit\_model_functions.py
This file implements the one-split and cross-validation training and testing algorithms for the RF model. Previous price data and technical indicators are input features while the direction of price movement is the output. After training the model, the function returns the testing prediction accuracy of the trained model.

### backtest.py
This file implements backtesting of the trained models. It takes a user input of a starting date and amount of cash investment and displays graphs showing the amount of profit/loss that would be incurred for each ticker input if the RF classifier model were used to determine investment decisions. If the classifier predicts the stock will go up, the stock will be bought; otherwise, it will be shorted. This allows profits/loss to be incurrened on both increase and decrease in stock price.

## Design Decisions
### Why use Python and the libraries that were selected?
I have past experience with the language and the sklearn library provides easily implementable machine learning functions. Libraries such as numpy and pandas make it easy to operate on stock data which is in a table format as well. 

Quandl was used because it retreives the necessary stock data (ex. opening/closing price, volume, etc.). It also supports retreiving fundamental data on stocks (ex. Price/Earnings ratio, debt/equity ratio, etc.) through Zack's if this project is expanded in the future. Ta-Lib was used because it has sufficient documentation and access to all the necessary technical indicators.
### Why use a Random Forest (RF) Classifier?
Based on [previous research](https://arxiv.org/ftp/arxiv/papers/1603/1603.00751.pdf), the RF classifier out performed other machine learning algorithms. Below is a screenshot of their results:

<img src="https://github.com/Bryanlee99/Stock_Forecasting_RF/blob/master/Images/RF_Classifier_Results.PNG" width="600">
*Source: Equity forecast: Predicting long term stock price movement using machine learning- Nikola Milosevic*

My tests comparing logistic regression, linear and quadratic discriminant analysis, and the RF classifier also showed the RF classifer had the most accurate prediction.

### Why technical and not fundamental analysis?
[Previous research](https://arxiv.org/ftp/arxiv/papers/1603/1603.00751.pdf) has employed fundamental analysis (using data on the company's financials in relation to stock price movements) to great success. However, the goal of this algorithm was to provide short-term stock movement forecasts, and that was best accomplished through technical analysis. However, fundamental analysis could be used to create quarterly price movement forecasts because the underlying financials of a company would likely affect long term stock price trends.

### Data Input Feature Selection
After determining that the algorithm would use technical indicators, I selected the most commonly used momentum indicators to employ the momentum trading strategy. This complements the short-term nature of the predictions because momentum is typically used over short time periods. Additionally, volume indicators were used (ex. Chaikin) because high volume levels support trends. Previous price data was also incorporated so additional underlying patterns in the price trends could be established. Through testing, 20 days was determined to provide a sufficient amount of previous information to improve prediction accuracy.

### Why were the data frames (for training and testing) segmented as they were ('stock_data', 'stock_returns', etc.)?
The categorization of data into these data frames was determined based on the data content and whether the data is input or output. Specifically, 'stock_data' and 'stock_returns inuitively held the unmodified retreived stock data and percent returns, respectively. The 'stock_lag' and 'stock_movement' data frames were input and output data, respectively. 'stock_lag' holds the shifted price data and technical indicators while 'stock_movement' holds 1's and -1's based on the direction of the stock movement.

### Why were the files segmented as they are?
The functions were split into python files based on their functionality. The *predict.py* is the primary file, while training methods are in the *fit_model_functions* and miscellaneous helper methods are included in the *helper_functions.py*. 

### Why was a UI not created?
This was primarily due to time constraints. An significant attempt was made to create a UI in Tkinter (the code created is included in the repository), but the graphics and functionality were subpar. 

## Algorithm Features
### Technical Indicators
The selection of the technical indicators is discussed in the above design section. In short, common momentum and volume indicators were used because they were indicative of momentum trends which is necessary for the momentum trading strategy. The frequently used nature of these indicators implies they are likely reputable. The parameters for each of the technical indicators are their defaults which on average should have the best performance.
### Random Forest Classifier Parameters
The parameters selected were primary default parameters. The number of estimators ('60') selected was determined using GridSearchCV which allows multiple values of the parameter to be input and the most accurate model to be selected out of those. The number of jobs ('n_jobs') allows for parallel computation. Two was selected for this algorithm for faster computation, but was not modified significantly because the algorithm generally ran quickly (<2 min).
### Time Series Cross Validation
Initially, I attempted to use normal cross-validation (sklearn 'cross_val_score'). However, this randomly permutes the stock price data and splits it into training and testing groups. This modifies the order of the stock data which makes the algorithm predictions unrealistically accurate because, for example, future data may be trained on while future data is used for testing. The results of this cross-validation for an early version of the algorithm are displayed below.
<img src="https://github.com/Bryanlee99/Stock_Forecasting_RF/blob/master/Images/normal_cv_results.png" width="600">
Instead, sklearn 'TimeSeriesSplit' was used for cross validation. This splits the data into equal proportions but only uses previous data for training and future data for testing. Results using this form of cross validation are displayed below.
<img src="https://github.com/Bryanlee99/Stock_Forecasting_RF/blob/master/Images/Forecaster_Sample_Output.PNG" width="600">
### Exponential Smoothing
Based on [previous research](https://arxiv.org/pdf/1605.00003.pdf), exponential smoothing was shown to increase RF classifier prediction accuracy. Smoothing weighs more recent stock prices higher than past stock prices. This increases prediction accuracy becauses current stock price predictions are desired, so more highly weighing current stock prices allows for more recent price trends to be emphasized.
### Additional Features
Aspects of the algorithm were designed to be dynamic. Specifically, a list of machine learning models (in *predict.py* under the 'Machine Learning Models' comment) is used so additional models (ex. support vector machines, decision trees, etc.) can be tested easily. Additionally, the array of features can be customized any of the technical indicators listed can be removed from the data by removing it from the 'feature_label_list'. Additionally, previous days' prices ('lag'), the start date of data collection ('start_date'), and the training proportion ('train_prop') can be modified in *predict.py* to optimize the algorithm. These values should not be changed by the user but can be flexibly changed by the code designer.
