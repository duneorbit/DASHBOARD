<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>
	
	<xsl:param name="overAllComments"/>
	<xsl:param name="teamComments"/>
	<xsl:param name="otherTeamComments"/>
	<xsl:param name="overAllJavaComments"/>
	<xsl:param name="overAllDataModelComments"/>
	<xsl:param name="overAllTeamJavaComments"/>
	<xsl:param name="overAllTeamDataModelComments"/>
	<xsl:param name="otherTeamJavaComments"/>
	<xsl:param name="otherTeamDataModelComments"/>
	<xsl:param name="overAllCommits"/>
	<xsl:param name="overAllJavaCommits"/>
	<xsl:param name="overAllDataModelCommits"/>

	<xsl:template match="CodeReviewSummary">
		<tbody>
			<tr><td>Overall comments</td><td><xsl:value-of select="$overAllComments"/></td><td>Total comments made</td></tr>
			<tr><td>Team comments</td><td><xsl:value-of select="$teamComments"/></td><td> Total comments made by TAJ team in response</td></tr>
			<tr><td>Other Team comments</td><td><xsl:value-of select="$otherTeamComments"/></td><td>Actual total comments from other teams</td></tr>
			<tr><td>Overall Java Comments</td><td><xsl:value-of select="$overAllJavaComments"/></td><td>Total Java comments made</td></tr>
			<tr><td>Overall Data Model Comments</td><td><xsl:value-of select="$overAllDataModelComments"/></td><td>Total data model comments made</td></tr>
			<tr><td>Overall Team Java Comments</td><td><xsl:value-of select="$overAllTeamJavaComments"/></td><td>Total Taj Java comments</td></tr>
			<tr><td>Overall Team Data Model Comments</td><td><xsl:value-of select="$overAllTeamDataModelComments"/></td><td>Total Taj data model comments</td></tr>
			<tr><td>Other Team Java comments</td><td><xsl:value-of select="$otherTeamJavaComments"/></td><td>Actual java comments from other teams</td></tr>
			<tr><td>Other Team SQL comments</td><td><xsl:value-of select="$otherTeamDataModelComments"/></td><td>Actual data model coments from other teams</td></tr>
			<tr><td>Overall java commits</td><td><xsl:value-of select="$overAllJavaCommits"/></td><td>Number of java commits for this time period</td></tr>
			<tr><td>Overall datamodel commits</td><td><xsl:value-of select="$overAllDataModelCommits"/></td><td>Number of datamodel commits for this time period</td></tr>
			<tr><td>Overall commits</td><td><xsl:value-of select="$overAllCommits"/></td><td>Number of commits for this time period</td></tr>
		</tbody>
	</xsl:template>
	
</xsl:stylesheet>