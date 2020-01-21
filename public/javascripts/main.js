if (document.querySelector('#place')) {
    document.querySelector('#place').addEventListener('click', setUrl);
} else if (document.querySelector('#move')) {
    document.querySelector('#move').addEventListener('click', setUrl2);
} else if (document.querySelector('#quit')){
    document.querySelector('#quit').addEventListener('click', quit);
}

function setUrl(){
    document.getElementById('form').action = document.getElementById('form').action + document.getElementById('figure').value;
}

function setUrl2(){
    document.getElementById('form').action = document.getElementById('form').action + document.getElementById('fromRow').value + "-" + document.getElementById('fromCol').value;
}

function quit(){
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
         document.body.innerHTML = "QUIT GAME";
        }
      };
      xhttp.open("DELETE", "/stratego/game", true);
      xhttp.send();
}
