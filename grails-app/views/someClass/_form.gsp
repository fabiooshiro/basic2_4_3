<%@ page import="basic2_4_3.SomeClass" %>



<div class="fieldcontain ${hasErrors(bean: someClassInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="someClass.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${someClassInstance?.title}"/>

</div>

