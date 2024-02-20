"use strict";

let engToUkrDaysMap = new Map();
let rowsAndExercises = new Map();
let rowId = 2;
let numberOfTrainingDays = 1;
let trainingDayBlank = [];
let exerciseBlank = {};

window.addEventListener('load', () => {
    rowsAndExercises.set(rowId, 1);

    for (let element of document.querySelectorAll(".row-2")) {
        trainingDayBlank.push(element.cloneNode(true));
    }

    let exerciseFieldAndDeleteDiv = document.querySelector(".exercise-field-and-delete");
    exerciseBlank.exerciseFieldAndDeleteDiv = exerciseFieldAndDeleteDiv.cloneNode(true);

    let numberField = document.querySelector(".number-field");
    exerciseBlank.numberField = numberField.cloneNode(true);

    initDaysMap();
});

function addTrainingDay(day) {
    if (numberOfTrainingDays >= 7)
        alert('Ви не можете створити більше 7 днів тренування')
    else {
        rowsAndExercises.set(++rowId, 1);
        numberOfTrainingDays++;

        let table = document.querySelector(".wrapper");
        for (let element of trainingDayBlank) {
            let elementCopy = element.cloneNode(true)
            elementCopy.className = elementCopy.className.replace("row-2", "row-" + rowId);

            table.appendChild(elementCopy);

            let classNameArr = elementCopy.className.split(" ");
            let [row, column] = classNameArr;
            let rowAndColumnSelector = "." + row + "." + column;
            let columnId = +elementCopy.className.charAt( elementCopy.className.length - 1 );

            switch (columnId) {
                case 1: {
                    setUpDayColumn(rowAndColumnSelector, day);
                    break;
                }
                case 2: {
                    setUpExerciseColumn(rowAndColumnSelector);
                    break;
                }
                case 3:
                case 4:
                case 5: {
                    setUpNumberColumn(rowAndColumnSelector);
                    break;
                }
            }

        }
    }
}

function deleteTrainingDay() {
    numberOfTrainingDays--;

    let button = document.activeElement;
    let container = button.parentElement;
    let currentRow = container.className.split(" ")[0];
    let currentRowId = +currentRow.split(" ")[1];

    for (let element of document.querySelectorAll("." + currentRow))
        element.remove();
    rowsAndExercises.delete(currentRowId); //  mb will need to be deleted
}

function addExercise() {
    let button = document.activeElement;
    let container = button.parentElement;
    let currentRow = container.className.split(" ")[0];

    let currentRowId = +currentRow.split("-")[1];
    let currentExerciseId = rowsAndExercises.get(currentRowId);

    rowsAndExercises.set(currentRowId, ++currentExerciseId);

    addExerciseFormDiv(currentRowId, container, button);
    addExerciseNumberFields(currentRowId);
}

function deleteExercise() {
    let button = document.activeElement;
    let exerciseFormDiv = button.parentElement;
    let container = exerciseFormDiv.parentElement;
    let currentRow = container.className.split(" ")[0];
    let currentRowId = +currentRow.split("-")[1];

    let exerciseIndex = getExerciseIndex(currentRowId, exerciseFormDiv);

    container.removeChild(exerciseFormDiv);
    deleteExerciseNumberFields(currentRowId, exerciseIndex);
}

function setRowsAndExercisesMap() {
    document.getElementById("map").value = JSON.stringify([...rowsAndExercises])
}

function initUpdatePage() {

}

function addExerciseToLastDay(jsonExercise) {
    let exercise = JSON.parse(jsonExercise);
    let containerColumn = "column-2"
    let lastRowId = rowId;
    let lastRow = "row-" + lastRowId;
    let currentExerciseId = rowsAndExercises.get(lastRowId);

    let containerSelector = "." + lastRow + "." + containerColumn;
    let buttonSelector = containerSelector + " > " + "input";

    let container = document.querySelector(containerSelector);
    let button = document.querySelector(buttonSelector);

    rowsAndExercises.set(lastRowId, ++currentExerciseId);

    addExerciseFormDiv(lastRowId, container, button, exercise);
    addExerciseNumberFields(lastRowId, exercise);
}

function deleteTheOnlyOneExercise() {
    let containerColumn = "column-2";
    let lastRowId = rowId;
    let lastRow = "row-" + lastRowId;
    let containerSelector = "." + lastRow + "." + containerColumn;
    let exerciseFormDivSelector = containerSelector + " > " + "div";

    let container = document.querySelector(containerSelector);
    let exerciseFormDiv = document.querySelector(exerciseFormDivSelector);

    let exerciseIndex = getExerciseIndex(lastRowId, exerciseFormDiv);

    container.removeChild(exerciseFormDiv);
    deleteExerciseNumberFields(lastRowId, exerciseIndex);
}

function deleteFirstTrainingDay() {
    numberOfTrainingDays--;
    rowsAndExercises.delete(2);

    let firstRow = "row-2";
    for (let element of document.querySelectorAll("." + firstRow))
        element.remove();
}








function getExerciseIndex(rowId, exerciseFormDiv) {
    let row = "row-" + rowId;
    let column = "column-2"
    let exerciseFormDivSelector = "." + row + "." + column + " > " + "div";
    let exerciseFormDivElements = document.querySelectorAll(exerciseFormDivSelector);

    let index = 0;
    for (let divElement of exerciseFormDivElements) {
        if (exerciseFormDiv === divElement)
            break;
        index++;
    }

    return index;
}

function deleteExerciseNumberFields(rowId, exerciseIndex) {
    let row = "row-" + rowId;

    for (let columnId = 3; columnId <= 5; columnId++) {
        let column = "column-" + columnId;
        let currentNumberFieldsSelector = "." + row + "." + column + " > " + "input";

        document
            .querySelectorAll(currentNumberFieldsSelector)
            .item(exerciseIndex)
            .remove();
    }
}

function generateHTMLNameId(baseName, rowId, exerciseId) {
    let HTMLName = baseName + "_" + rowId;
    if (exerciseId === undefined || exerciseId === null)
        return HTMLName;
    return HTMLName + "_" + exerciseId;
}


function addExerciseFormDiv(rowId, container, button, exercise) {
    let exerciseId = rowsAndExercises.get(rowId);
    let exerciseFormDivCopy = exerciseBlank.exerciseFieldAndDeleteDiv.cloneNode(true);
    for (let element of exerciseFormDivCopy.childNodes) {
        if (element.nodeName === "INPUT") {
            element.name = generateHTMLNameId("exerciseField", rowId, exerciseId);
            if (exercise !== undefined && exercise !== null) {
                element.value = exercise.name;
            }
        }
    }
    container.insertBefore(exerciseFormDivCopy, button);
}

function addExerciseNumberFields(rowId, exercise) {
    let row = "row-" + rowId;
    let exerciseId = rowsAndExercises.get(rowId);

    for (let columnId = 3; columnId <= 5; columnId++) {
        let currentColumn = "column-" + columnId;
        let currentNumberColumnSelector = "." + row + "." + currentColumn;

        let numberFieldCopy = exerciseBlank.numberField.cloneNode(true);
        numberFieldCopy.name = generateHTMLNameId("numberField", rowId, exerciseId);

        if (exercise !== undefined && exercise !== null) {
            switch (columnId) {
                case 3: { numberFieldCopy.value = exercise.numberOfSets }
                case 4: { numberFieldCopy.value = exercise.numberOfReps }
                case 5: { numberFieldCopy.value = exercise.workWeight }
            }
        }

        document
            .querySelector(currentNumberColumnSelector)
            .appendChild(numberFieldCopy);
    }
}

function setUpDayColumn(columnSelector, day) {
    let selectSelector = columnSelector + " > " + "select";
    let select = document.querySelector(selectSelector);
    select.name = generateHTMLNameId("dayOfWeek", rowId);

    let optionsSelector = selectSelector + " > " + "option";
    let options = document.querySelectorAll(optionsSelector);
    for (let option of options) {
        if (option.value === day)
            option.setAttribute("selected", "selected");
    }
}

function setUpExerciseColumn(columnSelector) {
    let selector = columnSelector + " > " + "div" + " > " + "input";
    let exerciseInput = document.querySelector(selector);
    let exerciseId = rowsAndExercises.get(rowId);

    exerciseInput.name = generateHTMLNameId("exerciseField", rowId, exerciseId);
}

function setUpNumberColumn(columnSelector) {
    let selector = columnSelector + " > " + "input";
    let numberInput = document.querySelector(selector);
    let exerciseId = rowsAndExercises.get(rowId);

    numberInput.name = generateHTMLNameId("numberField", rowId, exerciseId);
}

function initDaysMap() {
    engToUkrDaysMap.set("Monday", "Понеділок");
    engToUkrDaysMap.set("Tuesday", "Вівторок");
    engToUkrDaysMap.set("Wednesday", "Середа");
    engToUkrDaysMap.set("Thursday", "Четвер");
    engToUkrDaysMap.set("Friday", "П'ятниця");
    engToUkrDaysMap.set("Saturday", "Субота");
    engToUkrDaysMap.set("Sunday", "Неділя");
}
