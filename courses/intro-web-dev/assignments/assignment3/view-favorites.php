<!DOCTYPE html>
<html lang=en>
<head>
    <meta charset=utf-8>
</head>
<body >
<h1>Favourites Tab</h1>
<p>Below is a list of all your favourites</p>
<a href="browse-paintings.php">Back to Home</a><hr>



<?php     
    session_start();
    //Creates a box for each favorite 
    if(isset($_SESSION['painting'])){
        foreach(array_slice($_SESSION['painting'], 1) as $p){
            echo '<img src="images/art/works/square-small/'.$p['ifn'].'.jpg"><br>'; 
            echo '<a href="single-painting.php?id='.$p['id'].'"><strong>'.$p['title'].'</strong></a><br>';
            echo '<a href="remove-favorites.php?id='.$p['id'].'">Remove</a>';
            echo '<hr>';
        }
        echo '<a href="remove-favorites.php?id=0">Remove All Favorites</a>'; 
    }  
?>
</body> 
</body>