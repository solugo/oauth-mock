<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OAuth Mock Login</title>
    <style>
        body {
            padding: 0;
            margin: 0;
            width: 100dvw;
            height: 100dvh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .loginForm {
            background: #EEE;
            width: 40em;
            height: auto;
        }

        .controls {
            display: grid;
            margin: 1em;
            gap: 1em;
            grid-template-columns: 10em 1fr;
        }

        .actions {
            display: flex;
            margin: 1em;
            gap: 1em;
            justify-content: center;
        }
    </style>
</head>
<body>
<form class="loginForm" method="POST">
    <input name="redirect_url" type="hidden" value="${redirectUrl!"."}">
    <div class="controls">
        <label for="username">Username</label>
        <input id="username" name="username" type="text">

        <label for="subject">Subject</label>
        <input id="subject" name="subject" type="text">
    </div>
    <div class="actions">
        <button id="submit" type="submit">Login</button>
    </div>
</form>
</body>
</html>