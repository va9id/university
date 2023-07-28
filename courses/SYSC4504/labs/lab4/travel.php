<?php include 'functions.inc.php'; ?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">    
    <title>Share Your Travels</title>
      
    <link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/bootstrap-theme.css" />

</head>

<body>

    <?php include 'header.inc.php'; ?>

    <!-- Page Content -->
    <main class="container">
        <div class="row">
            <?php include 'left.inc.php'; ?>
            <div class="col-md-10">

                <div class="jumbotron" id="postJumbo">
                    <h1>Posts</h1>
                    <p>Read other travellers' posts ... or create your own.</p>
                    <p><a class="btn btn-warning btn-lg">Learn more &raquo;</a></p>
                </div>        
      
                <!-- start post summaries -->
                <div class="postlist">

                    <!-- replace each of these rows with a function call -->
					<div class="row">
						<div class="col-md-4">
							<a href="post.php?id=1" class=""><img src="images/8710320515.jpg" alt="Ekklisia Agii Isidori Church" class="img-responsive"></a>
						</div>
						<div class="col-md-8">
							<h2>Ekklisia Agii Isidori Church</h2>
							<div class="details">Posted by <a href="user.php?id=2" class="">Leonie Kohler</a>
								<span class="pull-right">2/8/2017</span>
								<p class="ratings"><img src="images/star-gold.svg" width="16"><img src="images/star-gold.svg" width="16"><img src="images/star-gold.svg" width="16"><img src="images/star-white.svg" width="16"><img src="images/star-white.svg" width="16"> 15 Reviews</p>
							</div>
							<p class="excerpt">At the end of the hot climb up to the top Lycabettus Hill you are greeted with the oasis that is the Ekklisia Agii Isidori church.</p>
							<p><a href="post.php?id=1" class="btn btn-primary btn-sm">Read more</a></p>
						</div>
					</div>
					<hr>

                </div>  <!-- end post list -->
            </div>  <!-- end col-mid-10 -->
        </div>  <!-- end row -->
    </main>
    

        <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
</body>

</html>