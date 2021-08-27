/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global element, Element */
var content = document.getElementById("title-content");
var x = document.getElementById("title-content").value;
var y = document.getElementById("name").value;
var z = document.getElementById("mail").value;
var w = document.getElementById("phonenum").value;
var q = document.getElementById("title").value;
content.addEventListener('focus', (event) => {
  document.getElementById("title-content").value = "";
});

content.addEventListener('blur', (event) => {
  document.getElementById("title-content").value = x;
});



