const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");

function dibuixa(){
    console.log("draw");
    let json = document.getElementById("currentJson").innerHTML;
    console.log("json: "+json);
    let figures = transformJSON(json);
    figures.forEach(draw);
}
export { dibuixa };
function transformJSON(json) {
    try {
        const parsedJson = JSON.parse(json);
            if (Array.isArray(parsedJson)) {
            return parsedJson;
        } else {
            console.error("Json no es un array vaild.");
            return [];
        }
    } catch (error) {
        console.error("Error al analitzar la cadena JSON:", error);
        return [];
    }
}

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
