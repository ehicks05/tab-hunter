<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="tabs" type="java.util.List<com.hicksteam.tab.db.gen.tables.pojos.Tab>"--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="section">
    <div class="container">
        <div class="columns is-multiline is-centered">
            <div class="column">
                <h1 class="title">${title}</h1>
                <table class="table is-narrow">
                    <thead>
                    <tr>
                        <th></th>
                        <th>artist</th>
                        <th>title</th>
                        <th class="has-text-right">votes</th>
                        <th class="has-text-right">rating</th>
                        <th>type</th>
                        <th class="has-text-right">views</th>
                        <%--<th>created on</th>--%>
                        <%--<th>authorId</th>--%>
                        <%--<th>source</th>--%>
                        <%--<th>hash</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="tab" items="${tabs}" varStatus="loop">
                        <tr>
                            <td class="has-text-right">${loop.count}.</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/artist?artist=${tab.artist}">${tab.artist}</a>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/tab?tabId=${tab.id}">${tab.title}</a></td>
                            <td class="has-text-right"><fmt:formatNumber value="${tab.votes}" pattern="#,###"/></td>
                            <td class="has-text-right"><fmt:formatNumber value="${tab.rating}" pattern="#.00"/></td>
                            <td>${tab.type}</td>
                            <td class="has-text-right">${tab.views}</td>
                            <%--<td><fmt:formatDate value="${tab.createdOn}" pattern="yyyy-MM-dd"/></td>--%>
                            <%--<td>${tab.authorId}</td>--%>
                            <%--<td class="has-text-right">${tab.source}</td>--%>
                            <%--<td class="has-text-right">${tab.contentHash}</td>--%>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</section>
