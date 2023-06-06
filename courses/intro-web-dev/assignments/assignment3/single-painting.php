<?php 

require_once('config.inc.php');
require_once('database.inc.php');

$pdo = setConnectionInfo(array(DBCONNECTION, DBUSER, DBPASS));

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
      if(null === filter_input(INPUT_GET, 'id', FILTER_VALIDATE_INT, FILTER_NULL_ON_FAILURE)){
        $_GET['id'] = 5; 
      }
      $painting = getPaintingDetails($_GET['id']);
      $p = $painting->fetch(); 

      $genreInfo = getGenres($_GET['id']);
      $subjectInfo = getSubjects($_GET['id']);
      $stars = getAvgStars($_GET['id']); 
      $s = $stars->fetch(); 
      $reviewInfo = getReviews($_GET['id']); 
      $frameInfo = getFrames($_GET['id']); 
      $glassInfo = getGlass($_GET['id']); 
      $mattInfo = getMatt($_GET['id']); 
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
              <i class="grid layout icon"></i>
              Browse
                <i class="dropdown icon"></i>
              <div class="menu">
                <a class="item"><i class="users icon"></i> Artists</a>
                <a class="item"><i class="theme icon"></i> Genres</a>
                <a class="item" href="browse-paintings.php?"><i class="paint brush icon"></i> Paintings</a>
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
    
<main >
    <!-- Main section about painting -->
    <section class="ui segment grey100">
        <div class="ui doubling stackable grid container">
		
            <div class="nine wide column">
                <?php 
                    echo '<img src="images/art/works/medium/'.$p['ImageFileName'].'.jpg" alt="..." class="ui big image" id="artwork">';
                ?> 
                <div class="ui fullscreen modal">
                  <div class="image content">       
                      <div class="description">
                      <p></p>
                    </div>
                  </div>
                </div>                
                
            </div>	<!-- END LEFT Picture Column --> 
			
            <div class="seven wide column">
                
                <!-- Main Info -->
                <div class="item">
					<h2 class="header"><?php echo $p['Title']; ?></h2>
					<h3><?php echo $p['LastName']; ?></h3>
					<div class="meta">
						<p>
                            <?php 
                                $emptyStars = 5 - $s['Stars']; 
                                for($i = 0; $i < $s['Stars']; $i++){
                                    echo '<i class="orange star icon"></i>';
                                }
                                for($j=0; $j < $emptyStars; $j++){
                                    echo '<i class="empty star icon"></i>';
                                }
                            ?>	
						</p>
						<p><?php echo $p['Excerpt']; ?></p>
					</div>  
                </div>                          
                  
                <!-- Tabs For Details, Museum, Genre, Subjects -->
                <div class="ui top attached tabular menu ">
                    <a class="active item" data-tab="details"><i class="image icon"></i>Details</a>
                    <a class="item" data-tab="museum"><i class="university icon"></i>Museum</a>
                    <a class="item" data-tab="genres"><i class="theme icon"></i>Genres</a>
                    <a class="item" data-tab="subjects"><i class="cube icon"></i>Subjects</a>    
                </div>
                
                <div class="ui bottom attached active tab segment" data-tab="details">
                    <table class="ui definition very basic collapsing celled table">
					  <tbody>
						  <tr>
						 <td>
							  Artist
						  </td>
						  <td>
                            <?php echo '<a href="'.$p['ArtistLink'].'">'. $p['LastName']. '</a>'; ?> 
						  </td>                       
						  </tr>
						<tr>                       
						  <td>
							  Year
						  </td>
						  <td>
							<?php echo $p['YearOfWork'];?>
						  </td>
						</tr>       
						<tr>
						  <td>
							  Medium
						  </td>
						  <td>
                              <?php echo $p['Medium']; ?> 
						  </td>
						</tr>  
						<tr>
						  <td>
							  Dimensions
						  </td>
						  <td>
                            <?php echo $p['Width'] . "cm x " . $p['Height'] . "cm"; ?>
						  </td>
						</tr>        
					  </tbody>
					</table>
                </div>
				
                <div class="ui bottom attached tab segment" data-tab="museum">
                    <table class="ui definition very basic collapsing celled table">
                      <tbody>
                        <tr>
                          <td>
                              Museum
                          </td>
                          <td>
                            <?php echo $p['GalleryName']; ?> 
                          </td>
                        </tr>       
                        <tr>
                          <td>
                              Assession #
                          </td>
                          <td>
                          <?php echo $p['AccessionNumber']; ?> 
                          </td>
                        </tr>  
                        <tr>
                          <td>
                              Copyright
                          </td>
                          <td>
                          <?php echo $p['CopyrightText']; ?> 
                          </td>
                        </tr>       
                        <tr>
                          <td>
                              URL
                          </td>
                          <td>
                          <?php echo '<a href="'.$p['MuseumLink'].'">View painting at museum site</a>' ; ?> 
                            
                          </td>
                        </tr>        
                      </tbody>
                    </table>    
                </div>     
                <div class="ui bottom attached tab segment" data-tab="genres">
 
                        <ul class="ui list">
                            <?php 
                                foreach($genreInfo as $g){
                                    echo '<li class="item"><a href="'. $g['Link'].'">'. $g['GenreName'].'</a></li>'; 
                                }
                            ?>
                        </ul>

                </div>  
                <div class="ui bottom attached tab segment" data-tab="subjects">
                    <ul class="ui list">
                        <?php 
                            foreach($subjectInfo as $s){
                                echo '<li class="item"><a href="#">'.$s['SubjectName'].'</a></li>'; 
                            }
                        ?>
                        </ul>
                </div>  
                <!-- Cart and Price -->
                <div class="ui segment">
                    <div class="ui form">
                        <div class="ui tiny statistic">
                          <div class="value">
                            <?php echo "$". $p['ROUND(Cost)'];?> 
                          </div>
                        </div>
                        <div class="four fields">
                            <div class="three wide field">
                                <label>Quantity</label>
                                <input type="number">
                            </div>                               
                            <div class="four wide field">
                                <label>Frame</label>
                                <select id="frame" class="ui search dropdown">
                                    <option>None</option>
                                    <?php 
                                        foreach($frameInfo as $f){
                                            echo '<option>'.$f['Title'].'</option>';
                                        }
                                    ?>
                                </select>
                            </div>  
                            <div class="four wide field">
                                <label>Glass</label>
                                <select id="glass" class="ui search dropdown">
                                    <option>None</option>
                                    <?php 
                                        foreach($glassInfo as $g){
                                            echo '<option>'.$g['Title'].'</option>';
                                        }
                                    ?>
                                </select>
                            </div>  
                            <div class="four wide field">
                                <label>Matt</label>
                                <select id="matt" class="ui search dropdown">
                                    <option>None</option>
                                    <?php 
                                        foreach($mattInfo as $m){
                                            echo '<option>'.$m['Title'].'</option>';
                                        }
                                    ?>
                                </select>
                            </div>           
                        </div>                     
                    </div>
                    <div class="ui divider"></div>
                    <button class="ui labeled icon orange button">
                      <i class="add to cart icon"></i>
                      Add to Cart
                    </button>
                    <?php
                    echo '<a class="ui right labeled icon button" href="addToFavorites.php?id='.$_GET['id'].'&ifn='.$p['ImageFileName'].'&title='.$p['Title'].'">';
                    echo '<i class="heart icon"></i>Add to Favorites</a>';
                    ?> 
                </div>     <!-- END Cart -->                      
                          
            </div>	<!-- END RIGHT data Column --> 
        </div>		<!-- END Grid --> 
    </section>		<!-- END Main Section --> 
    
    <!-- Tabs for Description, On the Web, Reviews -->
    <section class="ui doubling stackable grid container">
        <div class="sixteen wide column">
        
            <div class="ui top attached tabular menu ">
              <a class="active item" data-tab="first">Description</a>
              <a class="item" data-tab="second">On the Web</a>
              <a class="item" data-tab="third">Reviews</a>
            </div>
			
            <div class="ui bottom attached active tab segment" data-tab="first">
                <?php echo $p['Description'] ?>
            </div>	<!-- END DescriptionTab --> 
			
            <div class="ui bottom attached tab segment" data-tab="second">
				<table class="ui definition very basic collapsing celled table">
                  <tbody>
                      <tr>
                     <td>
                          Wikipedia Link
                      </td>
                      <td>
                        <?php echo '<a href="'.$p['WikiLink'].'">View painting on Wikipedia</a>'; ?>
                      </td>                       
                      </tr>                       
                      <tr>
                     <td>
                          Google Link
                      </td>
                      <td>
                        <?php echo '<a href="'.$p['GoogleLink'].'">View painting on Google Art Project</a>';?>      
                      </td>                       
                      </tr>
                      <tr>
                     <td>
                          Google Text
                      </td>
                      <td>
                        <?php echo $p['GoogleDescription'];?>
                      </td>                       
                      </tr>                      
                  </tbody>
                </table>
            </div>   <!-- END On the Web Tab --> 
			
            <div class="ui bottom attached tab segment" data-tab="third">                
				<div class="ui feed">
                        <?php 
                            $len = count($reviewInfo);
                            $lastComment = 0;
                            foreach($reviewInfo as $r){

                                echo '<div class="event">';
                                echo '<div class="content">';
                                echo '<div class="date">'.$r['ReviewDate'].'</div>';
                                echo '<div class="meta">';
                                echo '<a class="like">';
                                for($i=0;$i<$r['Rating']; $i++){
                                    echo '<i class="star icon"></i>';
                                }
                                for($j=0;$j< 5 - $r['Rating'];$j++){
                                    echo '<i class="empty star icon"></i>';
                                }
                                echo '</a>';
                                echo '</div>';
                                echo '<div class="summary">';
                                echo $r['Comment'];
                                echo '</div>';
                                echo '</div>';
                                echo '</div>';
                                if($lastComment != $len - 1){
                                    echo '<div class="ui divider"></div>';    
                                }
                                $lastComment++; 
                            }
                        ?>                             
                </div>   <!-- END Reviews Tab -->          
            </div>        
    </section> <!-- END Description, On the Web, Reviews Tabs --> 
    <!-- Related Images ... will implement this in assignment 2 -->    
    <section class="ui container">
    <h3 class="ui dividing header">Related Works</h3>        
	</section>  
</main>    
  <footer class="ui black inverted segment">
      <div class="ui container">footer</div>
  </footer>
</body>
</html>