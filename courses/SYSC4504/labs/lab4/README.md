# Lab 4: Server-Side Programming using PHP

## Problem Statement 

1. Implement generateLink(), it takes three arguments `$url`, `$label`, and `$class`, and will echo a properly formed hyperlink in the following form:
    ```
    <a href=”$url” class= “$class”>$label</a>
    ```

2. Implement outputPostRow(), it takes a single argument: `$number` and echoes the necessary markup for a single post.

3. Complete the last function, which again takes only a single parameter, a number between 0 and 5. It outputs that number of gold star image elements, followed by a number of what star images. The total number of gold and white star images needs to add up to 5. Make use of that function in `outputPostRow()`.

## Usage 

- Setup an XAMPP Apache Server and then access the lab at http://localhost/travel.php