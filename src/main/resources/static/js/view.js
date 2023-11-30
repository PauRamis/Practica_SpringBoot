const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");

function dibuixa(){
    let jsonElement = document.getElementById("currentJson");
    console.log("jsonElement:" + jsonElement);
    let jsonString = decodeEntities(jsonElement.textContent);
    console.log("jsonString:" + jsonString);
    let json = JSON.parse(jsonString);
    console.log("pasredJSON: " + json);
    console.log("stringifiedJSON: " + JSON.stringify(json));
    json.forEach(draw);
}

function decodeEntities(encodedString) {
    var textArea = document.createElement('textarea');
    textArea.innerHTML = encodedString;
    return textArea.value;
}

export { dibuixa };

import {doFigura} from '/js/doDraw.js';

function draw(figure){
console.log(figure);

figure = {"type":"triangle","x":412,"y":150,"size":"50","color":"#ff0000","name":"defaultName","fill":false};
console.log(figure.fill);


    const fill = figure.fill;
    const x = figure.x;
    const y = figure.y;
    const size = figure.size;
    const color = figure.color;
    const type = figure.type;

    doFigura(fill, x, y, size, color, type);
}
