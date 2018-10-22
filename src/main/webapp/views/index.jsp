<%--@elvariable id="tabs" type="java.util.List<com.hicksteam.tab.db.gen.tables.pojos.Tab>"--%>

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
        <div class="columns is-multiline is-centered">
            <div class="column">
                <h1 class="title">Search Results</h1>
                <table class="table is-narrow">
                    <tr>
                        <td>id</td>
                        <td>authorId</td>
                        <td>artist</td>
                        <td>title</td>
                        <td>votes</td>
                        <td>rating</td>
                        <td>type</td>
                        <td>created on</td>
                        <td>views</td>
                    </tr>
                    <c:forEach var="tab" items="${tabs}">
                        <tr>
                            <td>${tab.id}</td>
                            <td>${tab.authorId}</td>
                            <td>${tab.artist}</td>
                            <td><a href="${pageContext.request.contextPath}/tab?tabId=${tab.id}">${tab.title}</a></td>
                            <td>${tab.votes}</td>
                            <td>${tab.rating}</td>
                            <td>${tab.type}</td>
                            <td>${tab.createdOn}</td>
                            <td>${tab.views}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>
</body>
</html>