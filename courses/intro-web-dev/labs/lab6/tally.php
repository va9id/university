<!DOCTYPE html>
<html lang=en>
<head>
    <meta charset=utf-8>
</head>
<body>
    <h1>QuickPoll Tally</h1>
    <p>Your answer has been registered. The current totals are:</p>
    <p>Yes: 
    <?php
        session_start();
        echo $_SESSION['question']['y'];
    ?><p>
    <p>No: 
    <?php
        session_start();
        echo $_SESSION['question']['n'];
    ?><p>
    <a href="vote.php">Vote Again</a><br>
    <a href="lab6.html">Register a new question</a>
</body>
</html>