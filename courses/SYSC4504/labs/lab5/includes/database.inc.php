<?php
// Collection of functions to deal with SQL database

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
function runQuery($pdo, $sql, $parameters=array())     {
    //global $pdo; 
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

//Generates a query to get all employees
function readAllEmployees() {
    $query = "SELECT EmployeeID, FirstName, LastName FROM Employees ORDER BY LastName";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll();     
}

//Generates a query to get the employee specificied by a last name
function readSelectEmployeesByName($EmployeeName) {
    $query = "SELECT EmployeeID, FirstName, LastName FROM Employees
            WHERE LastName = :name ORDER BY LastName";
    global $pdo; 
    $results = runQuery($pdo, $query, array($EmployeeName));
    return $results->fetchAll();    
}

//Generates a query to get information about an employee given their ID number
function readSelectEmployeeByID($EmployeeID) {
    $query = "SELECT FirstName, LastName, Employees.Address, City, Region, Country, Postal, Email 
            FROM Employees WHERE EmployeeID=" . $EmployeeID . "";
    global $pdo; 
    return runQuery($pdo, $query);
    
}

//Generates a query to get the ToDo list of an employee given their ID
function readTODOs($EmployeeID) {
    $query = "SELECT DateBy, EmployeeToDo.Status, Priority, EmployeeToDo.Description 
            FROM EmployeeToDo INNER JOIN Employees ON (EmployeeToDo.EmployeeID = Employees.EmployeeId) 
            WHERE Employees.EmployeeID=" . $EmployeeID . " ORDER BY DateBy";
    global $pdo; 
    $results = runQuery($pdo, $query);
    return $results->fetchAll(); 
    
}



?>
