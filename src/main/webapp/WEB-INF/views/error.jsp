<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layout.jsp">
	<jsp:param name="pageTitle" value="Something went wrong"/>
	<jsp:param name="content" value="error_content.jsp"/>
</jsp:include>
