const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");
let firstCall = true;
let x2 = null;
let y2 = null;

//Dibux a ma
let isDrawing = false;
let coordenades = [];
let figure;

export function startDrawing() {
console.log("startDrawing");
    isDrawing = true;
    ctx.beginPath();
}

export function keepDrawing(e) {
  if (isDrawing == true) {
    console.log("keepDrawing");
     var x = event.offsetX;
     var y = event.offsetY;

     coordenades.push({ x, y });

     ctx.strokeStyle = document.getElementById("color").value;
     ctx.lineTo(x, y);
     ctx.stroke();
  }
}

export function stopDrawing() {
console.log("stopDrawing");
  if (isDrawing == false) {
      return null;
  }
  isDrawing = false;
  figure = {
      type: "handDrawing",
      x: coordenades,
      y: null,
      size: null,
      color: ctx.strokeStyle,
      name: "handDrawing",
      fill: false,
  };
  //Reiniciam les coordenades
  coordenades = [];
}

export function getFigure(){
    let retunFigure = figure;
    figure = null;
    return retunFigure;
}

export function render(figures){
    console.log("render");
    console.log(figures);

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    console.log("RenderClear");
    firstCall = true;
    console.log(firstCall);
    figures.forEach(doFigura);
}

//Dibuixar figures
function doFigura(currentFigure){
    console.log("currentFigure");
    console.log(currentFigure);
    let type = currentFigure.type;
    let x = currentFigure.x;
    let y = currentFigure.y;
    let size = currentFigure.size;
    let color = currentFigure.color;
    let name = currentFigure.name;
    let fill = currentFigure.fill;

    if (type == "handDrawing"){
        drawHandDrawing(x, color);
        drawHandDrawing(x, color);

    } else if (type == "line"){
        doLine(x, y, color);

    } else if (type == "quadrat"){
        square(fill, x, y, size, color);

    } else if (type == "triangle"){
        triangle(fill, x, y, size, color);

    } else if (type == "cercle"){
        cercle(fill, x, y, size, color);

    } else if (type == "estrella"){
        estrella(fill, x, y, size, color);
    }
}

//A partir de una figura, dibuixarla a ma
function drawHandDrawing(points, color) {
    ctx.strokeStyle = color;
    for (let i = 0; i < points.length - 1; i++) {
        ctx.beginPath();
        ctx.moveTo(points[i].x, points[i].y);
        ctx.lineTo(points[i + 1].x, points[i + 1].y);
        ctx.stroke();
    }
}

//Dibuxar linies
export function doLine(x1, y1, color){
    console.log("doTrueLine");
    if (Array.isArray(x1)){
        //Si x1 es un array, el conjunt és x1:[x1, x2], y1:[y1, y2]
        console.log("LineArr");
        x2 = x1[1];
        y2 = y1[1];
        x1 = x1[0];
        y1 = y1[0];

    }
    if(x2 == null){
        //Si x2 es null, es el primer click
        console.log("First call");
        x2 = x1;
        y2 = y1;
    } else {
        //Dibuixar la linia
        console.log("Second call");
        ctx.strokeStyle = color;
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.lineTo(x2, y2);
        ctx.stroke();

        figure = {
          type: "line",
          x: {x1, x2},
          y: {y1, y2},
          size: null,
          color: ctx.strokeStyle,
          name: "line",
          fill: false,
        };
        x2 = null;
        y2 = null;
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
        doFill(color);
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
        doFill(color);
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
        doFill(color);
    }
    ctx.closePath();
    ctx.stroke();
}

//Omplir de blanc
function doFill(color){
    ctx.fillStyle = color;
    ctx.fill();
}