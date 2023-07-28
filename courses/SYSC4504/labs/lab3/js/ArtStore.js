var currTotal =0;
for( var i = 0; i<3; i++){
    currTotal += calculateTotal(quantities[i], prices[i]);
    outputCartRow(filenames[i], titles[i], quantities[i], prices[i], calculateTotal(quantities[i], prices[i]));
}

//Subtotal
document.write('<tr class ="totals"><td colspan = "4">Subtotal</td>');
document.write("<td> $"+ currTotal.toFixed(2) + "</td></tr>");

//Tax
var tax = (0.1*currTotal);
document.write('<tr class ="totals"><td colspan = "4">Tax</td>' );
document.write("<td> $" + tax.toFixed(2) + "</td></tr>");

//Shipping
var shipping = 0; 
if (currTotal < 1000){
    shipping = 40;
} 
document.write('<tr class ="totals"><td colspan = "4">Shipping</td>');
document.write("<td> $"+ shipping.toFixed(2) + "</td></tr>");

//Grand Total
document.write('<tr class="totals focus"><td colspan="4">Grand Total</td>');
document.write("<td> $"+ (currTotal + tax + shipping).toFixed(2) +"</td></tr>");
