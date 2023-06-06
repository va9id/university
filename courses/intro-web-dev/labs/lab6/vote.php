<!DOCTYPE html>
<html lang=en>
<head>
    <meta charset=utf-8>
</head>
<body>
    <h1>QuickPoll Vote</h1>
    <form method="get" action="addVote.php">
        <?php 
        session_start();
        echo $_SESSION['question']["q"] .'<br>'; 
        ?>
        <input type="radio" id="y" name="vote" value="yes">
        <label for="y">Yes</label><br> 
        <input type="radio" id="n" name="vote" value="no">
        <label for="n">No</label><br> 
        <input type="submit" value="Register my vote">
    </form>
</body>
</html>
