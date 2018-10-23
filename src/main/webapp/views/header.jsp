<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    var myAC;
    var artist;
    var title;
    
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

        function search(query, callback) {

            fetch("${pageContext.request.contextPath}/ajaxSearch?query=" + query)
                .then(function(response) {
                    return response.json();
                })
                .then(function(myJson) {
                    // console.log(JSON.stringify(myJson));
                    callback(myJson);
                });
        }

        // initialize algolia autocomplete
        myAC = autocomplete('#searchInput', { hint: false, cssClasses: {}, minLength: 2 }, [
            {
                source: search,
                displayKey: 'display',
                templates: {
                    suggestion: function(suggestion) {
                        return suggestion.display;
                    }
                }
            }
        ]).on('autocomplete:selected', function(event, suggestion, dataset) {
            console.log(suggestion, dataset);
            artist = suggestion.artist;
            title = suggestion.title;
        });

        document.getElementById('searchButton').addEventListener('click', function (e) {
            goToTab();
        });

        document.getElementById('searchInput').addEventListener('keypress', function (e) {
            if (event.key === 'Enter')
                goToTab();
            else
            {
                artist = '';
                title = '';
            }

        });
    });

    function goToTab()
    {
        var term = document.getElementById('searchInput').value;
        location.href = "${pageContext.request.contextPath}/search?query=" + term + "&artist=" + artist + "&title=" + title;
    }
</script>

<style>
    .algolia-autocomplete {
        width: 100%; color:black;
    }
    .algolia-autocomplete .aa-input, .algolia-autocomplete .aa-hint {
        width: 100%;
    }
    .algolia-autocomplete .aa-hint {
        color: #999;
    }
    .algolia-autocomplete .aa-dropdown-menu {
        width: 100%;
        background-color: #fff;
        border: 1px solid #999;
        border-top: none;
    }
    .algolia-autocomplete .aa-dropdown-menu .aa-suggestion {
        cursor: pointer;
        padding-bottom: .375em;
        padding-left: .625em;
        padding-right: .625em;
        padding-top: .375em;
        /*font-weight: bold;*/
        font-style: normal;
        color:black;

        font-size: 1rem;
        line-height: 1.5;
    }
    .algolia-autocomplete .aa-dropdown-menu .aa-suggestion.aa-cursor {
        background-color: #B2D7FF;
    }
</style>

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
                            <button id="searchButton" class="button is-primary">
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