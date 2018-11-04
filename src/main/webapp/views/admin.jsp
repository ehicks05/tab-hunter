<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="inc_header.jsp"/>
    <title>Tab Hunter</title>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            document.getElementById('adminSearchButton').addEventListener('click', function (e) {
                doAdminSearch();
            });

            document.getElementById('adminSearchInput').addEventListener('keypress', function (e) {
                if (event.key === 'Enter')
                    doAdminSearch();
                else
                {
                    artist = '';
                    name = '';
                }

            });
        });

        function doAdminSearch()
        {
            var term = document.getElementById('adminSearchInput').value;
            var url = "${pageContext.request.contextPath}/admin/search?query=" + encodeURIComponent(term);
            console.log(url);
            location.href = url;
        }
    </script>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">

                <div class="field has-addons">
                    <p class="control">
                        <input id="adminSearchInput" class="input" type="text" placeholder="Find a tab">
                    </p>
                    <p class="control">
                        <button id="adminSearchButton" class="button is-primary">
                            Search
                        </button>
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>


<jsp:include page="footer.jsp"/>
</body>
</html>