# Stock Forecasting with a Random Forest Classifier
Stock price forecasting using a random forest (RF) classifier

## Overview
This program is a stock price forecasting algorithm. It uses a RF classifier that is trained and tested using stock price and volume data ranging from a given start date to end date. Training features include stock price data and multiple technical indicators. The stock price forecast period can be customized. The algorithm takes an array of stock tickers as input. Each ticker in the array is trained and tested independently.

For each ticker, the algorithm outputs a graph of the average prediction accuracy of the ticker's test set over the test period and for the desired number of forecast days.

The following technical indicators were used as input features: 
* Volume, Exponential Moving Average (EMA), Bollinger Bands, MACD, Commodity Channel Index (CCI), Relative Strength Index (RSI), Williams %R, Chaikin Oscillator, Slow Stochastic Oscillator

## Usage
1. Copy the files in this repository
2. Install *requirements.txt* to obtain the necessary python libraries (all of which are listed in the "Additional Information" section below).
*Note:* If the 'Ta-Lib' library does not install properly, follow the steps under 'Ta-Lib Install'
2. Open *parameters.txt* and run with the default parameters. 
*Note:* After running the program once, you can input custom parameters (of the format "[Parameter-name]: [value]"). Notes on proper parameter format is included at the top of the text file. The *sample_inputs.txt* file can be referenced for functional sample inputs.
3. Run *predict.py* to display graphs showing the average forecasting results and the hypothetical earnings from each ticker if the RF classifier was used.

Ta Lib Install:
1. Go to [this link](https://www.lfd.uci.edu/~gohlke/pythonlibs/#ta-lib) to download the proper Ta-Lib wrapper (.whl file). Already included in the repository are the 'Ta-Lib Wrappers' folder are the .whl files for install on 64-bit Windows for Python 2.7, 3.4, 3.5, and 3.6.
2. Run 'pip install (whl-file-name).whl', where 'whl-file-name' is the name of the .whl file corresponding to your Python version.

## Additional Information
### Requirements
* Python 3
* Matplotlib
* Numpy
* Sklearn
* Pandas
* Quandl
* Ta-Lib
