<?php 

session_start(); 

//creates session the first time a favourite is added
if(!isset($_SESSION['painting'])){
    $_SESSION['painting'][] = array();
}

//Keeps track of amount of paintings in favourites 
if(!($_SESSION['count'])){
    $_SESSION['count'] = 0;
}

//Add the favorite to the session 
if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['id']) && isset($_GET['ifn']) && isset($_GET['title'])){
        //Add 
        if(!array_key_exists($_GET['id'], $_SESSION['painting'])){
            $id = $_GET['id']; 
            $_SESSION['painting'][$id] = array("id" => $_GET['id'], "ifn" => $_GET['ifn'], "title" => $_GET['title']);        
            $_SESSION['count']++; 
        } 
        header('Location: view-favorites.php');
    }
}



?>