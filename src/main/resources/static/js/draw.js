const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");
let figures = [];
let figure;

//Seleccionam figura per defecte
let toolDefault = document.getElementById("ma");
      toolDefault.checked = true;
let tool = "ma";
hideTools();

function getTool(){
    let thisTool = document.querySelector('input[name="tool"]:checked').value;
    return thisTool;
}

function showTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "block";
}

function hideTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "none";
}
import {startDrawing, draw, stopDrawing, doLine, doFigura} from '/js/doDraw.js';
canvas.addEventListener("mousedown", function (event) {
    const boundingRect = canvas.getBoundingClientRect();
    const x = event.offsetX;
    const y = event.offsetY;
    //Ens aseguram que l'acció es un click esquerre
    if (event.button == 0) {
        //Usam l'eina triada
        tool = getTool();
        let color = document.getElementById("color").value;
        let fill = document.getElementById("fill").checked;
        switch(tool){
            case "ma":
                startDrawing(event);
                canvas.onmousemove = draw;
                canvas.onmouseup = stopDrawing;
                break;

            case "linia":
                doLine(x, y, color);
                //Guardam la figura
                figure = {
                    type: "line",
                    x: x,
                    y: y,
                    size: null,
                    color: color,
                    name: "line",
                    fill: fill,
                };
                figures.push(figure);
                console.log(figures);
                document.getElementById("drawingInput").value = JSON.stringify(figures);
                break;

            case "figura":
                //Obtenim les dades
                let size = document.getElementById("fSize").value;
                let type = document.getElementById("tipusFigura").value;

                //Guardam la figura
                figure = {
                    type: type,
                    x: x,
                    y: y,
                    size: size,
                    color: color,
                    name: document.getElementById("fName").value,
                    fill: fill,
                };
                figures.push(figure);
                console.log(figures);
                document.getElementById("drawingInput").value = JSON.stringify(figures);

                //TODO render(figures);
                doFigura(fill, x, y, size, color, type);
                break;
        }
    }
});

document.getElementById("clear").onclick = function() {clear()};
//Esborrar
function clear() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    figures = [];
    console.log("cleared");
}
