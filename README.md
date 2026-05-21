# AEP 
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Last Commit](https://img.shields.io/github/last-commit/adammenkiel/AEP?style=for-the-badge) ![Activity](https://img.shields.io/github/commit-activity/m/adammenkiel/AEP?style=for-the-badge)

## Table of Contents
- [Introduction](#introduction)
- [Example effect](#example-effect)
- [Models](#models)
  - [StandardModel](#standardmodel)
  - [ExpressionStandardModel](#expressionstandardmodel)
  - [PoolESModel](#poolesmodel)
  - [StaticPoolModel](#staticpoolmodel)
  - [RegularPoolModel](#regularpoolmodel)
- [Data containers](#data-containers)
  - [Data Line Containers](#data-line-containers)
  - [Total Data Containers](#total-data-containers)
- [Testers](#testers)
- [Examples](#examples)
- [Demonstration class](#demonstration-class)
- [Tests](#tests)
- [Benchmark results](#benchmark-results)
- [Limitations and well-known problems](#limitations-and-well-known-problems)
- [Plans](#plans)
- [Licence](#licence)
- [Contact](#contact)
## Introduction

``AEP`` is an experimental library that implements several types of predictive models based on generating and scoring various mathematical expressions whose task is to match the result. Different models perform with varying effectiveness but the best of these models are ``ExpressionStandardModel`` and ``PoolESModel``. ``ExpressionStandardModel`` finds various math expressions and uses them for generating new ones which may be treated as new input data. ``PoolESModel`` is composed of two models, first model is called helpful model, it searches random math expressions and collect those whose test results are significant compared to other expressions, in new step it adds them to input data of ``ExpressionStandardModel``, that approach have high effectiveness.

Project despite temporary lack of some multi-thread support, includes optimizations for minimize multiple evaluations of the same expressions and reduce loops. As a result models such as ``ExpressionStandardModel`` can generate and score above 1 000 000 math expressions within 30 seconds, achieving 80%-90% accuracy for problem of distict a points with "1" classification inside wheel and "0" classification beyond of it while 100000 iterations. ``PoolESModel``, ignoring an initial expressions-finding phase achieves above 90% accuracy for the same problem while 100000 iterations. Project is generally working just for binary classification but it is actively being developed.

## Example effect:
<img width="500" height="500" alt="image" src="https://github.com/user-attachments/assets/3907b6e1-dcf7-4a8c-98c9-2da981b96036" />

## Models

### StandardModel
The easiest kind of model. It generates random, independent values, if generated math expression gets better test score than every expressions tested before then function ``foundResult`` is involved.

### ExpressionStandardModel
Model generates expressions from input data and expressions that were added to model's ``ProxyDataContainer``, evaluations of these expressions with variable values from data line under consideration will be treated as new parameter of this data line. It ensures the ability to apply transformations and combine added expressions by generating new ones using expressions added before, without direct implementation of genetic operators. Abstract functions ``foundResult`` and ``foundRandomExpression`` could be used for implementation when new expressions should be added (if it should be added) to ``ProxyDataContainer`` while program works.

### PoolESModel
At the beginning, this model is a composition of two models: ``StandardModel`` and ``ExpressionStandardModel``, ``StandardModel`` finds the threshold of acceptable test score of an expression by generating random expressions during fixed time period, the score of the best-graded solution during this period becomes that threshold value. In next step ``StandardModel`` searches n expressions with score better than fixed threshold (these expressions are independent), Finally StandardModel stops working and saves these expressions into ``ProxyDataContainer`` as new input data for ``ExpressionStandardModel`` that starts to search better solutions. Functions ``foundRandomExpression`` and ``foundResult`` are invoked just while ExpressionStandardModel working, so only after initial mathematics expressions are already founded. Data should be added by ``PoolESModel#addData`` method.


### StaticPoolModel
Similar idea to ``PoolESModel``, with that difference that in ``StaticPoolModel`` the threshold value is provided in the constructor.

### RegularPoolModel
Searches expressions but while the number of ones doesn't exceed n, the function foundResult won't be invoked, when the value of n is exceeded, function ``foundResult`` will be invoked for every found expression and the process starts over, expression can be saved into ``ProxyDataContainer`` as new input data.

## Data containers
### Data Line Containers
The project implements two types of data lines containers which store lines of dataset table (``VirtualDataLineContainer, StandardDataLineContainer``) and one (``ProxyDataLineContainer``) for math expressions.
Both of these types of containers support ``ProxyDataLineContainer``, these containers have freeze property which makes expression evaluation results will be stored in these containers as extension of line of dataset table directly so multiple calculations of same expression won't be required.
Moreover ``VirtualDataLineContainer`` minimizes loops execution because it's based on ``VirtualVariable`` that instead of value has a pointer to specific ``DataLineContainer`` and returns proper rawData or freezed Data value, so loops for updating values aren't required

### Total Data Containers
``TotalDataContainers`` are classes for creating new variables, parse variables into final form. Decision which total data container should be used depends on what kinds of variables are used for initial data. For example if initial data is defined as double vectors, ``DoubleVectorTotalContainer`` is required. If initial data is ``VirtualVariable``, ``VirtualTotalDataContainer`` is required.

## Testers
Project implements three kinds of tests for models.
``StandardNumberTest`` - Test for numbers without weights.
``WeightedNumberTest`` - Test for numbers with weightOne defined as reward for "1" classification, weightTwo defined as reward for "0" classification, weightMistake - punishment for mistake, punishment should be greater than the maximum of weightOne and weightTwo.
``StandardVectorTest`` - Test for vectors without weights.

## Examples

```java
package pl.publicprojects.predictor.basic.examples;

import org.slf4j.Logger;
import pl.publicprojects.language.interpreter.Interpreter;
import pl.publicprojects.language.interpreter.data.math.LanguageNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.DoubleNumber;
import pl.publicprojects.language.interpreter.data.math.number.numbers.IntegerNumber;
import pl.publicprojects.predictor.graph.TreeVertex;
import pl.publicprojects.predictor.model.data.TotalDataContainer;
import pl.publicprojects.predictor.model.data.container.StandardDataLineContainer;
import pl.publicprojects.predictor.model.data.container.ProxyDataLineContainer;
import pl.publicprojects.predictor.model.data.container.total.DoubleTotalDataContainer;
import pl.publicprojects.predictor.model.models.ExpressionStandardModel;
import pl.publicprojects.predictor.model.models.StandardModel;
import pl.publicprojects.predictor.model.tester.tests.StandardNumberTest;

import java.io.File;
import java.util.Scanner;

public class SMClusterExample {

    public static void main(String[] args) throws Exception {

        Interpreter interpreter = new Interpreter();
        ProxyDataLineContainer container = new ProxyDataLineContainer(interpreter);
        TotalDataContainer totalDataContainer = new DoubleTotalDataContainer(interpreter);

        StandardModel standardModel = new StandardModel(
                interpreter,
                totalDataContainer,
                new StandardNumberTest(totalDataContainer, interpreter)
        ) {

            private final Logger logger = ExpressionStandardModel.getLogger();

            @Override
            public void foundResult(double grade, TreeVertex vertex) {
                String code = vertex.toString();
                this.logger.info("Found expression: {} grade: {}", code, grade);
            }

            @Override
            public void foundRandomExpression(double grade, TreeVertex vertex) {}

            @Override
            public void loadData() throws Exception {
                File file = new File("datasets/result.txt");
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()) {
                    String[] lineArgs = scanner.nextLine().split(" ");
                    LanguageNumber<?>[] numberTable = new LanguageNumber<?>[1 + 2];

                    numberTable[0] = new IntegerNumber(Integer.parseInt(lineArgs[0]));

                    for(int i = 1; i <= 2; i++)
                        numberTable[i] = new DoubleNumber(Double.parseDouble(lineArgs[i]) / 10);

                    super.getTotalDataContainer().getRawData().add(
                            new StandardDataLineContainer(super.getTotalDataContainer(), numberTable, container)
                    );
                }
            }
        };
        container.setVariables(standardModel.getVariables());
        standardModel.loadData();
        standardModel.search();

    }
}
```
Rest examples could be found at ``src/main/pl/publicproject/examples``

## Demonstration class
Class ``RefactorClass``, the same as the examples, is not a part of this library, it is only for demonstration purposes. To change a series of math expressions from logs into one expression for classify data, ``RefactorClass`` can be used. Class could be found at ``src/main/pl/publicproject/examples/refactor``. Usage of this class is as follows:
- Copy each line of model logs
- Paste to input of ``RefactorClass``
- Write end + enter
Finally, we get a (not-simplified) result expression which is function of many variables, name of ones can be changed to arbitrary (for example use replace() function for do it). Condition expression > 0 should be classify input data, "1" classification if condition is true, "0" in another case.

## Tests
Results of tests confirm that ``ExpressionStandardModel`` achieves more than 80% accuracy during 100,000 iterations and ``PoolESResult`` achieves even more than 90% for the same amount of iterations of main-model (excluding the helpful-model iterations which generate fixed pool of independent expressions with the result better than average)
For convenience, all tests are placed in ``src/test/java``

## Benchmark results
Currently ``AEP`` project has two benchmarks implemented:
Benchmark                              Mode  Cnt    Score   Error  Units
ExpressGraphBenchmark.expressGenerate  avgt   25  189.455 ± 0.888  ns/op (time of generate one random tree)
ESModelBenchmark.modelRun                ss    5    3.098 ± 0.355   s/op (time of invoke 100,000 iterations in ExpressionStandardModel)

Detailed results could be found at CI logs:
https://github.com/adammenkiel/AEP/actions/

## Limitations and well-known problems
Evaluations (if nd4j is not used) are not performed in parallel because of lack of multi-thread support for the ``Interpreter`` and expression graphs. (multi-thread support requires abandoning the use of variables for evaluating expressions - data lines should be transferred to every place that requires accessing a value)

## Plans
One of the most important vectors of future development of this library is adding full support for parallel evaluation through the interpreter and graphs. Very important aspect of this project development is to improve quality of evaluations too, one of ways to project improvement is method inlining (test, etc...) using bytecode. Crucial issue is to increase the number of methods for testing and multi-class classification support and standardization of model usage.

## Licence
Project is based on Apache Licence 2.0. Licence could be found in LICENCE file.

## Contact
Email: akmenkiel@gmail.com

