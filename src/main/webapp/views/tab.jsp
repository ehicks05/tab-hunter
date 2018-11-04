<%--@elvariable id="tab" type="com.hicksteam.tab.db.gen.tables.pojos.Tab"--%>

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

    </script>
</head>
<body>

<jsp:include page="header.jsp"/>

<section class="section">
    <div class="container">
        <div class="columns">
            <h1 class="title">${tab.artist} - ${tab.name}</h1>
        </div>
    </div>
</section>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <pre>${tab.content}</pre>
            </div>
            <div class="column is-one-quarter">
                <table class="table">
                    <tbody>
                    <tr>
                        <td>URL</td>
                        <td><a target="_blank" href="${tab.url}">Link</a></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td>${tab.type}</td>
                    </tr>
                    <tr>
                        <td>Tonality</td>
                        <td>${tab.tonality}</td>
                    </tr>

                    <tr>
                        <td>Tuning</td>
                        <td>${tab.tuning}</td>
                    </tr>
                    <tr>
                        <td>Capo</td>
                        <td>${tab.capo}</td>
                    </tr>
                    <tr>
                        <td>Rating</td>
                        <td>${tab.rating}</td>
                    </tr>
                    <tr>
                        <td>Number Rates</td>
                        <td>${tab.numberRates}</td>
                    </tr>
                    <tr>
                        <td>Difficulty</td>
                        <td>${tab.difficulty}</td>
                    </tr>
                    <tr>
                        <td>Created On</td>
                        <td><fmt:formatDate value="${tab.createdOn}" pattern="MM/dd/yyyy"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>