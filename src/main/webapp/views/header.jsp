<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    document.addEventListener('DOMContentLoaded', function () {

        // Get all "navbar-burger" elements
        var $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

        // Check if there are any navbar burgers
        if ($navbarBurgers.length > 0) {

            // Add a click event on each of them
            $navbarBurgers.forEach(function ($el) {
                $el.addEventListener('click', function () {

                    // Get the target from the "data-target" attribute
                    var target = $el.dataset.target;
                    var $target = document.getElementById(target);

                    // Toggle the class on both the "navbar-burger" and the "navbar-menu"
                    $el.classList.toggle('is-active');
                    $target.classList.toggle('is-active');

                });
            });
        }

        // initialize
        var my_autoComplete = new autoComplete({
            selector: 'input[id="searchInput"]',
            minChars: 2,
            delay: 50,
            source: function (term, suggest) {

                fetch("${pageContext.request.contextPath}/search?query=" + term)
                    .then(function(response) {
                        return response.json();
                    })
                    .then(function(myJson) {
                        console.log(JSON.stringify(myJson));
                        suggest(myJson);
                    });

            }
        });
    });
</script>

<c:if test="${!empty responseMessage}">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column is-one-quarter">
                <div id="server-response-notification" class="notification is-hidden" style="position: fixed;top:90%;left:90%;transform: translate(-50%, -50%);z-index: 10;">
                    <button class="delete"></button>
                </div>
            </div>
        </div>
    </div>
</c:if>

<nav class="navbar is-dark" role="navigation" aria-label="main navigation">
    <div class="container">
        <div class="navbar-brand">
            <a class="navbar-item" href="${pageContext.request.contextPath}">
                <img src="${pageContext.request.contextPath}/resources/guitar.png" alt="Guitar" />
            </a>

            <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false" data-target="navMenu">
                <span aria-hidden="true"></span>
                <span aria-hidden="true"></span>
                <span aria-hidden="true"></span>
            </a>
        </div>
        
        <div class="navbar-menu" id="navMenu">
            <div class="navbar-start">
                <div class="navbar-item">
                    <div class="field has-addons autocomplete">
                        <p class="control">
                            <input id="searchInput" class="input" type="text" placeholder="Find a tab">
                        </p>
                        <p class="control">
                            <button class="button is-light">
                                Search
                            </button>
                        </p>
                    </div>
                </div>

            </div>
            <div class="navbar-end">
                <a class="navbar-item">
                    <div class="buttons">
                        <a class="button is-primary" href="${pageContext.request.contextPath}/signup">
                            <span class="icon has-text-light">
                                <i class="fas fa-plus-square"></i>
                            </span>
                            <strong>Sign Up</strong>
                        </a>

                        <a class="button is-light" href="${pageContext.request.contextPath}/logIn">
                            Log In
                        </a>
                    </div>
                </a>
            </div>
        </div>
    </div>
</nav>

<c:remove var="responseMessage" scope="session" />