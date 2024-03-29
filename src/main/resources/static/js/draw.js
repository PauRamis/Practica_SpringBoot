import {startDrawing, keepDrawing, stopDrawing, getFigure, doLine, render } from '/js/doDraw.js';

const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");
let figures = [];
let figure;
let isDrawingSaved = false;

var maCheckbox = document.getElementById('ma');
var liniaCheckbox = document.getElementById('linia');
var figuraCheckbox = document.getElementById('figura');

//Seleccionam figura per defecte
let toolDefault = maCheckbox;
      toolDefault.checked = true;
let tool = "ma";
hideTools();

//Per la pagina /edit
export function importFigures(imporedFigures){
    let wasPublic = document.getElementById("wasPublic");
    console.log("wasPublic");
    console.log(wasPublic.textContent);
    if (wasPublic == "true"){
        document.getElementById("isPublic").checked = true;
    }

    let curretDrawing = document.getElementById("currentJson");
    console.log(curretDrawing.value);

    figures = JSON.parse(curretDrawing.value);
    console.log(figures);

    let curretName = document.getElementById("currentName");
    document.getElementById("DrawingName").value = curretName.textContent;
    console.log(curretName.textContent);

    document.getElementById("drawingInput").value = JSON.stringify(figures);
    updateList();
}

function getTool(){
    let thisTool = document.querySelector('input[name="tool"]:checked').value;
    return thisTool;
}


maCheckbox.addEventListener("change", function (){
    hideTools();
    setStorage();
});
liniaCheckbox.addEventListener("change", function (){
    hideTools();
    setStorage();
});
figuraCheckbox.addEventListener("change", function (){
    showTools();
    setStorage();
});

function showTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "block";
}

function hideTools() {
  let x = document.getElementById("selectFigura");
      x.style.display = "none";
}



//Click al canvas
canvas.addEventListener("mousedown", function (event) {
    const boundingRect = canvas.getBoundingClientRect();
    const x = event.offsetX;
    const y = event.offsetY;
    setStorage();
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
                const mouseupListener = () => {
                    if (tool === "ma") {
                        canvas.removeEventListener('mouseup', mouseupListener); // Elimina el event listener
                        figure = getFigure();
                        if (figure === null) {
                            console.log("empty hand-drawing");
                        } else {
                            save(false, figure.x, null, null, color, "handDrawing");
                            figure = null;
                        }
                    }
                };
                canvas.addEventListener('mouseup', mouseupListener);
                break;

            case "linia":
                doLine(x, y, color);
                figure = getFigure();
                console.log("line figure: ");
                console.log(figure);
                if(figure != null){
                    saveWhole(figure);
                }
                break;

            case "figura":
                let size = document.getElementById("fSize").value;
                let type = document.getElementById("tipusFigura").value;
                save(fill, x, y, size, color, type);
                break;

        }
        fetchSave();
    }
});

function updateList(){
    console.log("updateList");
    let figureList = document.getElementById('figureList');
    figureList.innerHTML = '';
    figures.forEach(function(figure, index) {
        // Crea un element li i un button
        let listItem = document.createElement('li');
        let button = document.createElement('button');

        //Donar la funcio al button
        button.textContent = 'X';
        button.dataset.index = index;
        button.addEventListener('click', function() {
            //Obtenim l'index
            let currentIndex = parseInt(button.dataset.index, 10);

            // Elimina la figura
            figures.splice(currentIndex , 1);

            console.log("figures: ");
            console.log(figures);
            updateList();
            render(figures);
            document.getElementById("drawingInput").value = JSON.stringify(figures);
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
function saveWhole(figure){
    figures.push(figure);
    document.getElementById("drawingInput").value = JSON.stringify(figures);
    render(figures);
    updateList();
    //TODO save version
}

function save(fill, x, y, size, color, type){
    console.log("save");
    figure = {
        type: type,
        x: x,
        y: y,
        size: size,
        color: color,
        fill: fill,
    };
    saveWhole(figure);
    figure = null;
}

//TODO execute this every change
function fetchSave(){
    const data = {
        drawingInput: JSON.stringify(figures),
        drawingName: document.getElementById("DrawingName").value,
        isPublic: document.getElementById("isPublic").checked
    };
    console.log("fetchSave data:")
    console.log(data)

    let path = '/draw';
    if (isDrawingSaved){
        path = '/draw/version'
    }

    fetch(path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al guardar los datos');
        }
        isDrawingSaved = true;
        console.log('Datos guardados exitosamente');
    })
    .catch(error => {
        console.error('Error:', error);
    });

}

function updateVersion(data){
    //Todo fetch a controlador con drawingService.editDrawing
    //Need id

}

document.getElementById("clear").onclick = function() {clear()};
//Esborrar
function clear() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    let figureList = document.getElementById('figureList');
    figureList.innerHTML = '';
    figures = [];
    document.getElementById("drawingInput").value = JSON.stringify(figures);
    console.log("cleared");
}

function setStorage(){
    console.log("setStorage");

    //RadioButtons
    const subMenu = document.querySelector('.subMenu');
    const radioButtons = subMenu.querySelectorAll('input[type="radio"]');
    radioButtons.forEach(radioButton => {
        localStorage.setItem(radioButton.id, radioButton.checked);
    });

    //color
    const colorInput = document.getElementById('color');
        if (colorInput) {
            localStorage.setItem('colorValue', colorInput.value);
        }
    //tipus figura
    const tipoFiguraSelect = document.getElementById('tipusFigura');
    if (tipoFiguraSelect) {
        localStorage.setItem('tipoFiguraValue', tipoFiguraSelect.value);
    }

    //size
    const fSizeInput = document.getElementById('fSize');
    if (fSizeInput) {
        localStorage.setItem('fSizeValue', fSizeInput.value);
    }

    //fill
    const fillCheckbox = document.getElementById('fill');
    if (fillCheckbox) {
        localStorage.setItem('fillValue', fillCheckbox.checked);
    }
}
export function getStorage(){
    console.log("getStorage");

    //RadioButtons
    const subMenu = document.querySelector('.subMenu');
    const radioButtons = subMenu.querySelectorAll('input[type="radio"]');
    radioButtons.forEach(radioButton => {
        const value = localStorage.getItem(radioButton.id);
        if (value !== null) {
            console.log(radioButton.id)
            console.log(value);
            if(radioButton.id == "figura"){
               showTools();
            }
            radioButton.checked = value === 'true';
        }
    });

    //color
    const colorInput = document.getElementById('color');
    if (colorInput) {
        const colorValue = localStorage.getItem('colorValue');
        if (colorValue !== null) {
            colorInput.value = colorValue;
        }
    }

    //type
    const tipoFiguraSelect = document.getElementById('tipusFigura');
    if (tipoFiguraSelect) {
        const tipoFiguraValue = localStorage.getItem('tipoFiguraValue');
        if (tipoFiguraValue !== null) {
            tipoFiguraSelect.value = tipoFiguraValue;
        }
    }
    //fsize
    const fSizeInput = document.getElementById('fSize');
    if (fSizeInput) {
        const fSizeValue = localStorage.getItem('fSizeValue');
        if (fSizeValue !== null) {
            fSizeInput.value = fSizeValue;
        }
    }
    //fill
    const fillCheckbox = document.getElementById('fill');
    if (fillCheckbox) {
        const fillValue = localStorage.getItem('fillValue');
        if (fillValue !== null) {
            fillCheckbox.checked = fillValue === 'true';
        }
    }
}