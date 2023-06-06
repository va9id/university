<?php
/**
 * This file contains all the functions that interact with the databases. 
 * Contains functions to connect to database, run queries, and generate 
 * all the necessary queries to display all the correct information in
 * browse-paintings.php and single-paintings.php. 
 */

//Sets connection info to database
function setConnectionInfo($values=array()) {
    try{
        $connString = $values[0];
        $user = $values[1]; 
        $pass = $values[2]; 
        global $pdo; 
        $pdo = new PDO($connString, $user, $pass);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); 
        return $pdo;
    }
    catch(PDOException $e){
        die($e->getMessage());
    }
}

//Runs a given query in the database, returning the result set
function runQuery($pdo, $sql, $parameters=array()){
    if(count($parameters) == 1){
        $results = $pdo->prepare($sql);
        $results->bindValue(":name", $parameters[0]);
        $results->execute(); 
    }    
    else{
        $results = $pdo->query($sql); 
    }
    return $results; 
}

//Generates a query to get all artist names sorted by last name
function allArtistLastNames(){
    $query = "SELECT LastName FROM Artists ORDER BY LastName";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();     
}

//Generates a query to get all gallery names sorted in alphabetical order
function allGalleryNames(){
    $query = "SELECT GalleryName FROM Galleries ORDER BY GalleryName";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();    
}

//Generates a query to get all shapes sorted
function allShapeNames(){
    $query = "SELECT ShapeName FROM Shapes ORDER BY ShapeName";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();    
}

//Generates a query to get all paintings from provided artist last name filter
function filterName($lastName){
    $query = "SELECT Paintings.Title, Paintings.Excerpt, ROUND(Cost), Paintings.PaintingID, Paintings.ImageFileName, Artists.LastName
            FROM Paintings INNER JOIN Artists ON (Paintings.ArtistID = Artists.ArtistID) WHERE Artists.LastName='".$lastName."' 
            ORDER BY Title";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();    
}

//Generates a query to get all paintings from provided meseum filter
function filterMuseum($museumName){
    $query = "SELECT Paintings.Title, Paintings.Excerpt, ROUND(Cost), Paintings.PaintingID, Paintings.ImageFileName, Artists.LastName
            FROM Paintings INNER JOIN Artists ON (Paintings.ArtistID = Artists.ArtistID) INNER JOIN Galleries ON (Paintings.GalleryID = Galleries.GalleryID)
            WHERE Galleries.GalleryName='".$museumName."' ORDER BY Title";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();  
}  

//Generates a query to get all paintings from provided shape filter 
function filterShape($shapeName){
    $query = "SELECT Paintings.Title, Paintings.Excerpt, ROUND(Cost), Paintings.PaintingID, Paintings.ImageFileName, Artists.LastName
            FROM Paintings INNER JOIN Artists ON (Paintings.ArtistID = Artists.ArtistID) INNER JOIN Shapes ON (Paintings.ShapeID = Shapes.ShapeID) 
            WHERE Shapes.ShapeName='" . $shapeName . "' ORDER BY Title";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();    
}

//Generates a query to get first 20 paintings when no filter is chosen 
function getTwentyPaintings(){
    $query = "SELECT Title, Excerpt, ROUND(Cost), PaintingID, ImageFileName, Artists.LastName
            FROM Paintings LEFT JOIN Artists ON (Paintings.ArtistID = Artists.ArtistID) 
            WHERE Excerpt IS NOT NULL ORDER BY TITLE LIMIT 20";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();    
}

//Generates a query to get infomation about a painting's genre
function getGenres($id){
    $query = "SELECT Genres.GenreName, Genres.Link
    FROM Paintings 
    INNER JOIN (Genres INNER JOIN PaintingGenres ON Genres.GenreID=PaintingGenres.GenreID) 
    ON Paintings.PaintingID=PaintingGenres.PaintingID
    WHERE Paintings.PaintingID ='". $id ."'"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
}

//Generates a query to get infomation about a painting's subjects
function getSubjects($id){
    $query = "SELECT Subjects.SubjectName FROM Paintings 
    INNER JOIN (Subjects INNER JOIN PaintingSubjects ON Subjects.SubjectID=PaintingSubjects.SubjectID) 
    ON Paintings.PaintingID=PaintingSubjects.PaintingID
    WHERE Paintings.PaintingID ='". $id ."'"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
}

//Generates a query to get infomation about a painting's reviews
function getReviews($id){
    $query = "SELECT Reviews.Comment, Reviews.Rating, Reviews.ReviewDate
    FROM Reviews WHERE Reviews.PaintingID='".$id."' ORDER BY Reviews.Rating DESC"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
} 

//Generates a query to get infomation about a painting's average rating
function getAvgStars($id){
    $query = "SELECT ROUND(AVG(Reviews.Rating)) as Stars FROM Reviews WHERE Reviews.PaintingID='".$id."'";
    global $pdo; 
    return runQuery($pdo, $query);
}

//Generates a query to get infomation about a painting
function getPaintingDetails($id){
    $query = "SELECT Title, Excerpt, ImageFileName, Artists.LastName, Paintings.Description, GoogleLink, GoogleDescription, 
            WikiLink, ROUND(Cost), Artists.ArtistLink, YearOfWork, Width, Height, AccessionNumber, MuseumLink, 
            Medium,CopyrightText, Galleries.GalleryName
            FROM Paintings INNER JOIN Artists ON (Paintings.ArtistID = Artists.ArtistID)
            INNER JOIN Galleries ON (Paintings.GalleryID = Galleries.GalleryID)
            WHERE Paintings.PaintingID ='". $id ."'"; 
    global $pdo; 
    return runQuery($pdo, $query);
}

//Generates a query to get information about a painting's frames
function getFrames($id){
    $query = "SELECT DISTINCT TypesFrames.Title FROM Paintings 
            INNER JOIN (TypesFrames INNER JOIN OrderDetails ON TypesFrames.FrameID=OrderDetails.FrameID) 
            ON Paintings.PaintingID=OrderDetails.PaintingID
            WHERE Paintings.PaintingID ='". $id ."' AND TypesFrames.Title !='[None]' ORDER BY TypesFrames.Title"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
}

//Generates a query to get information about a painting's glass
function getGlass($id){
    $query = "SELECT DISTINCT TypesGlass.Title FROM Paintings 
            INNER JOIN (TypesGlass INNER JOIN OrderDetails ON TypesGlass.GlassID=OrderDetails.GlassID) 
            ON Paintings.PaintingID=OrderDetails.PaintingID
            WHERE Paintings.PaintingID ='". $id ."' AND TypesGlass.Title !='[None]' ORDER BY TypesGlass.Title"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
}

//Generates a query to get information about a painting's matt
function getMatt($id){
    $query = "SELECT DISTINCT TypesMatt.Title FROM Paintings 
    INNER JOIN (TypesMatt INNER JOIN OrderDetails ON TypesMatt.MattID=OrderDetails.MattID) 
    ON Paintings.PaintingID=OrderDetails.PaintingID
    WHERE Paintings.PaintingID ='". $id ."' AND TypesMatt.Title !='[None]' ORDER BY TypesMatt.Title"; 
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();
}

//Creates each painting block in the main page
function createPaintingList($p=array()){
    echo '<li class="item">'; 
    echo '<a class="ui small image" href="single-painting.php?id='.$p['PaintingID'].'">';
    echo '<img src="images/art/works/square-medium/'.$p['ImageFileName'].'.jpg"></a>'; 
    echo '<div class="content">';
    echo '<a class="header" href="single-painting.php?id='.$p['PaintingID'].'">'. $p['Title'].'</a>';
    echo '<div class="meta"><span class="cinema">'.$p['LastName'] .'</span></div>';
    echo '<div class="description">'; 
    echo '<p>'.$p['Excerpt'].'</p>';
    echo '</div>';
    echo '<div class="meta">'; 
    echo '<strong>$'.$p['ROUND(Cost)'].'</strong>';
    echo '</div>';
    echo '<div class="extra">';
    echo '<a class="ui icon orange button" href="cart.php?id='.$p['PaintingID'].'"><i class="add to cart icon"></i></a>';
    echo '<a class="ui icon button" href="addToFavorites.php?id='.$p['PaintingID'].'&ifn='.$p['ImageFileName'].'&title='.$p['Title'].'"><i class="heart icon"></i></a>';
    echo '</div>';
    echo '</div>';
    echo '</li>';
}

?>