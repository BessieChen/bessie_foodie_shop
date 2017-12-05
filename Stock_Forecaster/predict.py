"""
CS 50 Final Project
Title: Stock Price Prediction with a Random Forest Classifier
---
Author: Bryan Lee
Date: 12/4/2017
"""

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from Stock_Forecaster.fit_model_functions import fit_model, fit_model_cross_validation
from Stock_Forecaster.helper_functions import date_range, create_shifted_orderbook, read_txt, get_curr_date
from sklearn.ensemble import RandomForestClassifier

if __name__ == "__main__":
    stocks, period = read_txt('parameters.txt')
    # Technical indicator ist
    feature_label_list = ['Volume', 'EMA', 'upperband', 'middleband', 'lowerband', 'macdhist', 'CCI', 'RSI', 'WILLR',
                          'Chaikin', 'slowk', 'slowd']

    # List of stocks to input
    ticker_arr = stocks #['MMM', 'INTC', 'COHR']  # 'AMZN', 'ACN', 'AAP', 'AMG', 'ARE', 'LNT', 'MO']

    # Number of days' price data used for training
    lag = 20
    # Forward prediction period
    pred_period = period

    # Start and end dates of stock data set queried
    start_date = "Jun 1 2000"
    end_date = get_curr_date()

    # Proportion of data between the start date and end date used for training
    train_prop = 0.8
    test_prop = 1 - train_prop
    # Train-test splits for cross-validation
    splits = 3

    # Hit-Rate score for one train-test split
    hit_rate_one_split = []
    # Hit-Rate score for cross-validation results
    hit_rate_cv = []

    # Trains and tests on each ticker
    for ticker in ticker_arr:
        # Retrieves stock info and returns training/testing data
        stock_data, stock_returns, stock_lag, stock_movement = create_shifted_orderbook(ticker, start_date=start_date,
                                                                                        end_date=end_date,
                                                                                        lag_period=lag,
                                                                                        pred_period=pred_period)

        # Calculates the date that splits data into training and testing
        train_test_date = list(date_range(start_date, end_date, 0.9))[0]

        # Sets columns to retrieve data from
        train_test_indices = ['Train', 'Test']

        # Adds price features to feature list
        for i in range(0, lag):
            feature_label = 'Lag{:d}'.format(i)
            feature_label_list.append(feature_label)

        # Gets data with features from feature list
        stock_feature_df = stock_lag[feature_label_list]

        # Input training/testing data
        X = []
        X.append(stock_feature_df[stock_feature_df.index <= train_test_date].values)
        X.append(stock_feature_df[stock_feature_df.index > train_test_date].values)

        # Output training/testing data
        Y = []
        Y.append(stock_movement[stock_movement.index <= train_test_date].values)
        Y.append(stock_movement[stock_movement.index > train_test_date].values)

        # Gets output list of testing data
        test_output = Y[train_test_indices.index('Test')]

        # Machine learning model(s)
        models = [("RF", RandomForestClassifier(n_estimators=60, n_jobs=2, random_state=0))]

        # Trains and tests the model(s)
        results = []
        for model_name, model_type in models:
            results.append(fit_model(ticker, model_name, model_type, X[train_test_indices.index('Train')], \
                                     Y[train_test_indices.index('Train')], X[train_test_indices.index('Test')],
                                     test_output))
            output = fit_model_cross_validation(ticker, model_name, model_type, stock_feature_df.values, stock_movement.values,
                                                splits)

        # Adds the success rate for the ticker to the hit-rate lists
        hit_rate_one_split.append(results[0].values)
        hit_rate_cv.append(output)

    # Prints average success rate for one train-test split and cross-validation
    hit_rate_one_split_avg = np.mean(hit_rate_one_split, axis=0)
    hit_rate_cv = np.mean(hit_rate_cv, axis=0)
    hit_rate_cv_avg = np.mean(hit_rate_cv, axis=0)

    print("Avg. Hit-Rate (one split):")
    print(hit_rate_one_split_avg)

    print("Avg. Hit-Rate (cross-valid.):")
    print(hit_rate_cv_avg)

    # Plots the hit-rates for one train-test split per stock
    to_plot = hit_rate_one_split
    plt.ylabel('Accuracy')
    plt.xlabel('Forecast Day')
    plt.title('Stock Forecast')
    for y_arr, label in zip(to_plot, ticker_arr):
        plt.plot(np.arange(1, pred_period + 1), y_arr, label=label)
        plt.xticks(np.arange(1, pred_period + 1))
    plt.legend(bbox_to_anchor=(0, 1), loc=2, borderaxespad=0.)
    plt.ylim([0.4, 1])
    plt.show()

    # Plots the hit-rates for cross-validation per stock
    to_plot = hit_rate_cv
    plt.ylabel('Accuracy')
    plt.xlabel('Forecast Day')
    plt.title('Stock Forecast')
    for y_arr, label in zip(to_plot, ticker_arr):
        plt.plot(np.arange(1, pred_period + 1), y_arr, label=label)
        plt.xticks(np.arange(1, pred_period + 1))
    plt.legend(bbox_to_anchor=(0, 1), loc=2, borderaxespad=0.)
    plt.ylim([0.4, 1])
    plt.show(block=True)
