const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");

//Dibux a ma
let isDrawing = false;
export function startDrawing(e) {
    isDrawing = true;
    ctx.beginPath();
}

export function draw(e) {
  if (isDrawing == true) {
     var x = event.offsetX;
     var y = event.offsetY;

     ctx.strokeStyle = document.getElementById("color").value;
     ctx.lineTo(x, y);
     ctx.stroke();
  }
}

export function stopDrawing() {
  isDrawing = false;
}

//Dibuxar linies
let needStart = true;
export function doLine(x, y, color){
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
export function doFigura(fill, x, y, size, color, type){
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