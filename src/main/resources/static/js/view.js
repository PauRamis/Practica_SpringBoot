const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");

function dibuixa(){
    let jsonElement = document.getElementById("currentJson");
    let jsonString = decodeEntities(jsonElement.textContent);
    console.log(jsonString);
    let json = JSON.parse(jsonString);
    console.log("json: " + json);
    console.log("json: " + JSON.stringify(json));
    json.forEach(draw);
}

function decodeEntities(encodedString) {
    var textArea = document.createElement('textarea');
    textArea.innerHTML = encodedString;
    return textArea.value;
}

export { dibuixa };

import {doLine, doFigura} from '/js/doDraw.js';

function draw(figure){
    const fill = figure.fill;
        const x = figure.x;
        const y = figure.y;
        const size = figure.size;
        const color = figure.color;
        const type = figure.type;

        if(type == "line"){
            doLine(x, y, color);
            console.log("line");
        } else {
            doFigura(fill, x, y, size, color, type);
            console.log("figura");
        }
}
