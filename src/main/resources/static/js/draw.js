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

import {startDrawing, keepDrawing, stopDrawing, getFigure, doLine, render } from '/js/doDraw.js';

//Click al canvas
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
                canvas.onmousemove = keepDrawing;
                canvas.onmouseup = stopDrawing;
                canvas.addEventListener('mouseup', () => {
                    figure = getFigure();
                    if(figure == null){
                        console.log("empty");
                    } else {
                        save(false, figure.x, null, null, color, "handDrawing");
                    }
                });
                break;

            case "linia":
               // save(fill, x, y, null, color, "line");
                doLine(x, y, color);
                figure = getFigure();
                if(figure != null){
                    figures.push(figure);
                    document.getElementById("drawingInput").value = JSON.stringify(figures);
                    console.log(JSON.stringify(figures));
                    render(figures);
                }
                break;

            case "figura":
                let size = document.getElementById("fSize").value;
                let type = document.getElementById("tipusFigura").value;
                save(fill, x, y, size, color, type);
                render(figures);
                break;

        }

    }
});

//Todo evitar el segundo X
function updateList(){
    let figureList = document.getElementById('figureList');
    figureList.innerHTML = '';
    figures.forEach(function(figure, index) {
        if(figure.type == "linia"){
            //Todo unificar els 2 punts de una linia
        }

        // Crea un element li i un button
        let listItem = document.createElement('li');
        let button = document.createElement('button');

        //Donar format al button
        button.textContent = 'X';
        button.addEventListener('click', function() {
            // Elimina la figura
            figures.splice(index, 1);
            updateList();
            render(figures);
        });

        // Afegir el button al li
        listItem.appendChild(button);

        //Afegir text
        let text = document.createTextNode(figure.type);
        listItem.appendChild(text);

        // Agrega l'element
        figureList.appendChild(listItem);
    });
}

function save(fill, x, y, size, color, type){
    //Guardam la figura
    figure = {
        type: type,
        x: x,
        y: y,
        size: size,
        color: color,
        name: type,
        fill: fill,
    };
    figures.push(figure);
    document.getElementById("drawingInput").value = JSON.stringify(figures);
    console.log(JSON.stringify(figures));
    render(figures);
    updateList();
}

document.getElementById("clear").onclick = function() {clear()};
//Esborrar
function clear() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    let figureList = document.getElementById('figureList');
    figureList.innerHTML = '';
    figures = [];

    document.getElementById("drawingInput").value = JSON.stringify(figures);
    console.log(JSON.stringify(figures));
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