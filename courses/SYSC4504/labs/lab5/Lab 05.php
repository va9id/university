<?php

require_once('includes/book-config.inc.php');
require_once('includes/database.inc.php');

$pdo = setConnectionInfo(array(DBCONNECTION, DBUSER, DBPASS));

// if we have a non-empty search string, search for employee matches
if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['search']) && !empty($_GET['search']) ) {
   $employees = readSelectEmployeesByName($_GET['search']);  

}
else {
   // otherwise get all customers
   $employees = readAllEmployees();    
}   

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>Lab 05</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.1.3/material.blue_grey-orange.min.css">

    <link rel="stylesheet" href="css/styles.css">
    
    
    <script   src="https://code.jquery.com/jquery-1.7.2.min.js" ></script>
       
    <script src="https://code.getmdl.io/1.1.3/material.min.js"></script>
    
</head>

<body>
    
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-drawer
            mdl-layout--fixed-header">
            
    <?php include 'includes/header.inc.php'; ?>
    <?php include 'includes/left-nav.inc.php'; ?>
    
    <main class="mdl-layout__content mdl-color--grey-50">
        <section class="page-content">

            <div class="mdl-grid">

              <!-- mdl-cell + mdl-card -->
              <div class="mdl-cell mdl-cell--3-col card-lesson mdl-card  mdl-shadow--2dp">
                <div class="mdl-card__title mdl-color--orange">
                  <h2 class="mdl-card__title-text">Employees</h2>
                </div>
                <div class="mdl-card__supporting-text">
                    <ul class="demo-list-item mdl-list">

                         <?php  

                         foreach ($employees as $emp) {
                           echo '<li class="mdl-list__item">';
                           echo '<a href="Lab 05.php?employee=' . $emp['EmployeeID'] . '">' . $emp['FirstName'] . ' ' . $emp['LastName'] . '</a>';
                           echo '</li>    ';    
                        } 
                        ?>            


                    </ul>
                </div>
              </div>  <!-- / mdl-cell + mdl-card -->
              
              <!-- mdl-cell + mdl-card -->
              <div class="mdl-cell mdl-cell--9-col card-lesson mdl-card  mdl-shadow--2dp">
           
           
                 <?php
                 if ($_SERVER['REQUEST_METHOD'] == 'GET') {
                    if ( isset($_GET['employee']) ) {  

                       $results = readSelectEmployeeByID($_GET['employee']);
				  $requestedEmployee = $results->fetch();  
                  ?>
                  

                    <div class="mdl-card__title mdl-color--deep-purple mdl-color-text--white">
                      <h2 class="mdl-card__title-text">Employee Details</h2>
                    </div>
                    <div class="mdl-card__supporting-text">
                        <div class="mdl-tabs mdl-js-tabs mdl-js-ripple-effect">
                          <div class="mdl-tabs__tab-bar">
                              <a href="#address-panel" class="mdl-tabs__tab is-active">Address</a>
                              <a href="#todo-panel" class="mdl-tabs__tab">To Do</a>
                          </div>
                        
                          <div class="mdl-tabs__panel is-active" id="address-panel">
                              
                            <h4><?php echo $requestedEmployee['FirstName'] . ' ' . $requestedEmployee['LastName']; ?></h4>
                            <?php 
                                echo $requestedEmployee['Address'] . '<br>';
                                echo $requestedEmployee['City'] . ', ' . $requestedEmployee['Region'] . '<br>';         
                                echo $requestedEmployee['Country'] . ', ' . $requestedEmployee['Postal'] . '<br>'; 
                                echo $requestedEmployee['Email']
                             ?>                                
         
                          </div>
                          <div class="mdl-tabs__panel" id="todo-panel">
                              
                            <?php                       
                            
                               $todos = readTODOs (($_GET['employee']));                             
                               if ( is_null($todos) ) {  
                                    echo 'No To Dos for ' . $requestedEmployee['LastName'];        
                               } else {
                                ?>                                         
                            
                                   <table class="mdl-data-table  mdl-shadow--2dp">
                                  <thead>
                                    <tr>
                                      <th class="mdl-data-table__cell--non-numeric">Date</th>
                                      <th class="mdl-data-table__cell--non-numeric">Status</th>
                                      <th class="mdl-data-table__cell--non-numeric">Priority</th>
                                      <th class="mdl-data-table__cell--non-numeric">Content</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                   
                                     <?php foreach ($todos as $todo) { 
                            
                                        echo '<tr>';
                                        echo '<td class="mdl-data-table__cell--non-numeric">' . date("Y-M-d", strtotime($todo['DateBy'])) . '</td>';
                                        echo '<td class="mdl-data-table__cell--non-numeric">' . $todo['Status'] . '</td>';
                                        echo '<td class="mdl-data-table__cell--non-numeric">' . $todo['Priority'] . '</td>';
                                        echo '<td class="mdl-data-table__cell--non-numeric">' . substr($todo['Description'],1,50) . '</td>';
                                        echo '</tr>    ';  
                            
                                     } ?>                               
                            
                                  </tbody>
                                </table>
                             <?php  } ?>                              
         
                          </div>
                        </div>                         
                    </div>    
                    
                 <?php 
                    }  // end if ( isset ...
                 }  // // end if ( $_SERVER ... ?>     
                 
              </div>  <!-- / mdl-cell + mdl-card -->   
            </div>  <!-- / mdl-grid -->    

        </section>
    </main>    
</div>    <!-- / mdl-layout --> 
          
</body>
</html>