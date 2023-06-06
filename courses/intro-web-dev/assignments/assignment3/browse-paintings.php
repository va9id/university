<?php 

require_once('config.inc.php');
require_once('database.inc.php');

$pdo = setConnectionInfo(array(DBCONNECTION, DBUSER, DBPASS));

$topTwenty = false; 

$memcache = new Memcache;
$memcache->connect('localhost', 11211);

if(isset($_GET['artistFilter']) && $_GET['artistFilter'] !=0 ){
  // Get from cache if there already
  if($memcache->get($_GET['artistFilter']) != false){
    $paintings = $memcache->get($_GET['artistFilter']); 
  }
  //Not in cache, get results from database and cache them 
  else{
    $paintings = filterName($_GET['artistFilter']);  
    $memcache->set($_GET['artistFilter'], $paintings, false, 86400);
  }  
}
else if(isset($_GET['museumFilter']) && $_GET['museumFilter'] != 0){
  if($memcache->get($_GET['museumFilter']) != false){
    $paintings = $memcache->get($_GET['museumFilter']); 
  }
  else{
    $paintings = filterMuseum($_GET['museumFilter']);
    $memcache->set($_GET['museumFilter'], $paintings, false, 86400);
  }  
}
else if(isset($_GET['shapeFilter']) && $_GET['shapeFilter'] !=0 ){
  if($memcache->get($_GET['shapeFilter']) != false){ 
    $paintings = $memcache->get($_GET['shapeFilter']); 
  }
  else{
    $paintings = filterShape($_GET['shapeFilter']);
    $memcache->set($_GET['shapeFilter'], $paintings, false, 86400);
  }  
}
else{
  if($memcache->get('twenty') != false){ 
    $paintings = $memcache->get('twenty'); 
  }
  else{
    $paintings = getTwentyPaintings();
    $memcache->set('twenty', $paintings, false, 86400);
  }  
  $topTwenty = true; 
}

?>
<!DOCTYPE html>
<html lang=en>
<head>  
    <meta charset=utf-8>
    <link href='http://fonts.googleapis.com/css?family=Merriweather' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="css/semantic.js"></script>
    <script src="js/misc.js"></script>
    <link href="css/semantic.css" rel="stylesheet" >
    <link href="css/icon.css" rel="stylesheet" >
    <link href="css/styles.css" rel="stylesheet">
</head>
<body >
    
<header>
    <div class="ui attached stackable grey inverted  menu">
        <div class="ui container">
            <nav class="right menu">            
                <div class="ui simple  dropdown item">
                  <i class="user icon"></i>
                  Account
                    <i class="dropdown icon"></i>
                  <div class="menu">
                    <a class="item"><i class="sign in icon"></i> Login</a>
                    <a class="item"><i class="edit icon"></i> Edit Profile</a>
                    <a class="item"><i class="globe icon"></i> Choose Language</a>
                    <a class="item"><i class="settings icon"></i> Account Settings</a>
                  </div>
                </div>
                <a class=" item" href="view-favorites.php">
                  <i class="heartbeat icon"></i> Favorites
                  <?php 
                    session_start();
                    if(isset($_SESSION['count']) && $_SESSION['count'] > 0){
                      echo '('. $_SESSION['count'] . ')';
                    }
                  ?>
                </a>        
                <a class=" item">
                  <i class="shop icon"></i> Cart
                </a>                                     
            </nav>            
        </div>     
    </div>   
    <div class="ui attached stackable borderless huge menu">
        <div class="ui container">
            <h2 class="header item">
              <img src="images/logo5.png" class="ui small image" >
            </h2>  
            <a class="item">
              <i class="home icon"></i> Home
            </a>       
            <a class="item">
              <i class="mail icon"></i> About Us
            </a>      
            <a class="item">
              <i class="home icon"></i> Blog
            </a>      
            <div class="ui simple dropdown item">
                <!-- Browse menu is dynamic and should do stuff--> 
                <i class="grid layout icon"></i>
                Browse
                <i class="dropdown icon"></i>
              <div class="menu">
                <a class="item"><i class="users icon"></i> Artists</a>
                <a class="item"><i class="theme icon"></i> Genres</a>
                <a class="item"><i class="paint brush icon"></i> Paintings</a>
                <a class="item"><i class="cube icon"></i> Subjects</a>
              </div>
            </div>        
            <div class="right item">
                <div class="ui mini icon input">
                  <input type="text" placeholder="Search ...">
                  <i class="search icon"></i>
                </div>
            </div>      
        </div>
    </div>       
</header> 
    
<main class="ui segment doubling stackable grid container">
    <section class="five wide column">
        <form class="ui form" method="GET">
          <h4 class="ui dividing header">Filters</h4>
          <div class="field">
            <label>Artist</label>
            <!-- Dynamic drop down menu, shoud list all artist names--> 
            <select class="ui fluid dropdown" name="artistFilter">
                <option value="0">Select Artist</option>  
                <?php 
                  //Get artists names from cache
                  if($memcache->get('AD') != false){ 
                    $artists = $memcache->get('AD'); 
                  }
                  //Artist names not in cache, get results and cache them 
                  else{
                    $artists = allArtistLastNames(); //queury for all artist sorted by last name 
                    $memcache->set('AD', $artists, false, 86400);
                  }        
                  foreach ($artists as $artistName){      //Create an <option> for each one 
                      echo "<option";
                      if(isset($_GET['artistFilter']) && $_GET['artistFilter'] == $artistName['LastName']){
                        echo " selected"; 
                      }
                      echo ">". $artistName['LastName'] ."</option>";
                  }
                ?>
            </select>
          </div>  
          <div class="field">
            <label>Museum</label>
            <!-- Dynamic drop down menu, should list all museums --> 
            <select class="ui fluid dropdown" name="museumFilter">
                <option value="0">Select Museum</option>  
                <?php 
                  //Get museum names from cache
                  if($memcache->get('MD') != false){ 
                    $museums = $memcache->get('MD'); 
                  }
                  //museum names not in cache, get results and cache them 
                  else{
                    $museums = allGalleryNames();           //queury for all galleries sorted by name 
                    $memcache->set('MD', $museums, false, 86400);
                  } 
                  foreach ($museums as $museumName){     //Create an <option> for each one 
                      echo "<option";
                      if(isset($_GET['museumFilter']) && $_GET['museumFilter'] == $museumName['GalleryName']){
                        echo " selected"; 
                      }
                      echo ">". $museumName['GalleryName'] ."</option>";
                  }  
                ?>
            </select>
          </div>   
          <div class="field">
            <label>Shape</label>
            <!-- dynamic drop down menu, should list all shapes--> 
            <select class="ui fluid dropdown" name="shapeFilter">
                <option value="0">Select Shape</option>  
                <?php 
                  //Get shapes  from cache
                  if($memcache->get('SD') != false){ 
                    $shapes = $memcache->get('SD'); 
                  }
                  //shapes not in cache, get results and cache them 
                  else{
                    $shapes = allShapeNames();      //queury for all shapes sorted by name
                    $memcache->set('SD', $shapes, false, 86400);
                  }
                  foreach ($shapes as $shape){   //Create an <option> for each one 
                      echo "<option";
                      if(isset($_GET['shapeFilter']) && $_GET['shapeFilter'] == $shape['ShapeName']){
                        echo " selected"; 
                      }
                      echo ">". $shape['ShapeName'] ."</option>";
                  }  
                ?>
            </select>
          </div>   
            <!-- Dynamic button that allows filters -->
            <button class="small ui orange button" type="submit">
                <i class="filter icon"></i> Filter 
            </button>    
        </form>
    </section>
    

    <section class="eleven wide column">
        <h1 class="ui header">Paintings</h1>
        <ul class="ui divided items" id="paintingsList">
        <!-- EACH "item" should be dynamically generated --> 
        <?php  
            if($topTwenty){
              echo "<h5>ALL PAINTINGS [TOP 20]</h5>";
            }
            foreach($paintings as $p){
                createPaintingList($p); 
            }
        ?>   
        </ul>        
    </section>  
    
</main>    
  <footer class="ui black inverted segment">
      <div class="ui container">footer for later</div>
  </footer>
</body>
</html>