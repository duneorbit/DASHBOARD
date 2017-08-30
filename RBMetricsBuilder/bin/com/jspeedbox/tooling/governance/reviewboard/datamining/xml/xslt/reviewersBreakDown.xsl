<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="reviewersMetrics">
		<tbody>
			<xsl:for-each select="./totals/entry">
				<xsl:choose>
					<xsl:when test="./value/@developerReply='false'"> 
						<tr>
							<td><xsl:value-of select="position()"/></td>
							<td><xsl:value-of select="./key"/></td>
							<td><xsl:value-of select="./value/@peformanceComments"/></td>
							<td><xsl:value-of select="./value/@cosmeticComments"/></td>
							<td><xsl:value-of select="./value/@miscComments"/></td>
							<td><xsl:value-of select="./value/@total"/></td>
							<td><xsl:value-of select="./value/@duplicateComments"/></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr bgcolor="#fcefed">
							<td><xsl:value-of select="position()"/></td>
							<td><xsl:value-of select="./key"/></td>
							<td><strong>-</strong></td>
							<td><strong>-</strong></td>
							<td><strong>-</strong></td>
							<td><xsl:value-of select="./value/@total"/></td>
							<td>-</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:for-each>
		</tbody>
	</xsl:template>
</xsl:stylesheet>