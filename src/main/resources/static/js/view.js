const canvas = document.querySelector("canvas");
const ctx = canvas.getContext("2d");
import {render} from '/js/doDraw.js';

function dibuixa(){
    //Descodificam el JSON i feim un draw a cada figura
    let jsonElement = document.getElementById("currentJson");
    console.log("jsonElement:" + jsonElement);
    let jsonString = decodeEntities(jsonElement.textContent);
    if (jsonString=="") {
        console.log("jsonString: empty");
    } else  {
        console.log("jsonString:" + jsonString);
        let json = JSON.parse(jsonString);
        console.log("pasredJSON: " + json);
        console.log("stringifiedJSON: " + JSON.stringify(json));
        document.getElementById("currentJson").value = JSON.stringify(json);

        render(json);
    }
}

function decodeEntities(encodedString) {
    var textArea = document.createElement('textarea');
    textArea.innerHTML = encodedString;
    return textArea.value;
}

export { dibuixa };

