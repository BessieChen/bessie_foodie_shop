# Algorithm Design
Description of key design decisions and aspects of algorithm function.

## Design Decisions
### Why use Python and the libraries that were selected?
I have past experience with the language and the sklearn library provides easily implementable machine learning functions. Libraries such as numpy and pandas make it easy to operate on stock data which is in a table format as well. 

Quandl was used because it retreives the necessary stock data (ex. opening/closing price, volume, etc.). It also supports retreiving fundamental data on stocks (ex. Price/Earnings ratio, debt/equity ratio, etc.) through Zack's if this project is expanded in the future. Ta-Lib was used because it has sufficient documentation and access to all the necessary technical indicators.
### Why use a Random Forest (RF) Classifier?
Based on [previous research](https://arxiv.org/ftp/arxiv/papers/1603/1603.00751.pdf), the RF classifier out performed other machine learning algorithms. My tests comparing logistic regression, linear and quadratic discriminant analysis, and the RF classifier also showed the RF classifer had the most accurate prediction.

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
