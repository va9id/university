/* define functions here */

function calculateTotal(quantity, price){
    return quantity * price; 
}

function outputCartRow(file, title, quantity, price, total){
    document.write('<tr class="products">'); 
    document.write("<td>" + '<img src="images/' + file + '">' + "</td>");
    document.write("<td>" + title +"</td>");
    document.write("<td>" + quantity +"</td>");
    document.write("<td>" + "$" + price.toFixed(2) +"</td>");
    document.write("<td>" + "$" + total.toFixed(2) +"</td>");
    document.write("</tr>");
}



        
