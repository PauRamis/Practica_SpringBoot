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

//Dibux a ma
let isDrawing = false;
function startDrawing(e) {
    isDrawing = true;
    ctx.beginPath();
}

function draw(e) {
  if (isDrawing == true) {
     var x = event.offsetX;
     var y = event.offsetY;

     ctx.strokeStyle = document.getElementById("color").value;
     ctx.lineTo(x, y);
     ctx.stroke();
  }
}

function stopDrawing() {
  isDrawing = false;
}

//Dibuxar linies
let needStart = true;
function doLine(x, y, color){
    if(needStart){
        ctx.strokeStyle = color;
        ctx.beginPath();
        ctx.moveTo(x, y);
        needStart = false;
    } else {
        ctx.lineTo(x, y);
        ctx.stroke();
        needStart = true;
    }
}

//Dibuixar figures
function doFigura(fill, x, y, size, color, type){
    if (type == "quadrat"){
        square(fill, x, y, size, color);

    } else if (type == "triangle"){
        triangle(fill, x, y, size, color);

    } else if (type == "cercle"){
        cercle(fill, x, y, size, color);

    } else if (type == "estrella"){
        estrella(fill, x, y, size, color);
    }
}

//pintar quadrat
function square(fill, x, y, size, color){
    //Ajustam les coordenades perque apareixi al centre del ratoli
    let half=size/2;
    x = x-half;
    y = y-half;

     if(fill == true){
        ctx.fillStyle = color;
        ctx.fillRect(x, y, size, size);
     } else {
        ctx.strokeStyle = color;
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(x+ +size, y);
        ctx.lineTo(x+ +size, y+ +size);
        ctx.lineTo(x, y+ +size);
        ctx.lineTo(x, y);
     }
    ctx.stroke();
}

//pintar triangle
function triangle(fill, x, y, size, color){
    //Ajustam les coordenades perque apareixi al centre del ratoli
        let half=size/2;
        x = x-half;
        y = y-half;

    ctx.strokeStyle = color;
    ctx.beginPath();
    ctx.moveTo(x+ +size/2, y);
    ctx.lineTo(x+ +size, y+ +size);
    ctx.lineTo(x, y+ +size);
    ctx.closePath();
    if(fill == true){
        doFill();
    }
    ctx.stroke();

}

//Cercle
function cercle(fill, x, y, size, color){
    let half = +size/2;
    ctx.beginPath();
    ctx.strokeStyle = color;
    ctx.ellipse(x, y, half, half, 0, 0, 2*Math.PI);
    if(fill == true){
        doFill();
    }
    ctx.stroke();
}

//Estrella de 7
function estrella(fill, x, y, size, color){
    ctx.beginPath();
    ctx.strokeStyle = color;
    size = size/2; //Per que la estrella no sigui més gran que les altres
    for (let i = 0; i < 14; i++) {
        let angle = Math.PI / 7 * i;
        let factor = (i % 2 === 0) ? 0.5 : 1; // Alterna la longitud dels punts per formar la estrella
        let posicioX = x + Math.cos(angle) * size * factor;
        let posicioY = y + Math.sin(angle) * size * factor;
        if (i === 0) {
            ctx.moveTo(posicioX, posicioY);
        } else {
            ctx.lineTo(posicioX, posicioY);
        }
    }

    if(fill == true){
        doFill();
    }
    ctx.closePath();
    ctx.stroke();
}

//Omplir de blanc
function doFill(){
    ctx.fillStyle = document.getElementById("color").value;
    ctx.fill();
}

document.getElementById("clear").onclick = function() {clear()};
//Esborrar
function clear() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    figures = [];
    console.log("cleared");
}
