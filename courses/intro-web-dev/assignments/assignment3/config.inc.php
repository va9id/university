<?php
define('DBCONNECTION', 'mysql:host=localhost;dbname=art');
define('DBUSER', 'testuser');
define('DBPASS', 'mypassword');

/* localhost mysql setup */
$pdo = new PDO(DBCONNECTION, DBUSER, DBPASS);

?>
