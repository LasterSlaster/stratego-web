if (document.querySelector('#place')) {
    document.querySelector('#place').addEventListener('click', setUrl);
} else if (document.querySelector('#move')) {
    document.querySelector('#move').addEventListener('click', setUrl2);
}

function setUrl(){
    document.getElementById('form').action = document.getElementById('form').action + document.getElementById('figure').value;
}

function setUrl2(){
    document.getElementById('form').action = document.getElementById('form').action + document.getElementById('fromRow').value + "-" + document.getElementById('fromCol').value;
}
