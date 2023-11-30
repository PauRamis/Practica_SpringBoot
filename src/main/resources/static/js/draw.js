const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");
let figures = [];
let figure;

var maCheckbox = document.getElementById('ma');
var liniaCheckbox = document.getElementById('linia');
var figuraCheckbox = document.getElementById('figura');

//Seleccionam figura per defecte
let toolDefault = maCheckbox;
      toolDefault.checked = true;
let tool = "ma";
hideTools();

function getTool(){
    let thisTool = document.querySelector('input[name="tool"]:checked').value;
    return thisTool;
}


maCheckbox.addEventListener("change", function (){
    hideTools();
});
liniaCheckbox.addEventListener("change", function (){
    hideTools();
});
figuraCheckbox.addEventListener("change", function (){
    showTools();
});

function showTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "block";
}

function hideTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "none";
}
import { startHandDrawing, addPointToHandDrawing, redrawHandDrawing, finishHandDrawing, doLine, doFigura } from '/js/doDraw.js';

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
                startHandDrawing(x, y, color);
                canvas.onmouseup = finishHandDrawing(figures);
                canvas.mouseleave = finishHandDrawing(figures);
                figure = //TODO recuperar figure;
                figures.push(figure);
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
                console.log(JSON.stringify(figures));
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
                    name: document.getElementById("DrawingName").value,
                    fill: fill,
                };
                figures.push(figure);
                console.log(figures);
                document.getElementById("drawingInput").value = JSON.stringify(figures);
                console.log(JSON.stringify(figures));
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

////LocalStorage, guardar en local////
//localStorage.setItem("json", JSON.stringify(figura))
//let jsonString = JSON.parse(localStorage.getItem("json"))


/*document.querySelector("select").value =
localStorage.getItem("figure") || ""*/

/*document.querySelector("select").addEventListener("change", (e) => {
    localStorage.setItem("figure", document.querySelector("select").value)
});*/

////Promise////
/*
fetch(url)
    then((response) => response.json())
    then((body) => console.log(body))
    catch((error) => console.error(error));
//Vs Await
try {
    const response = await fetch(url);
    const body = await response.json();
    console.log(body);
} catch (error) {
    console.log(error);
}*/