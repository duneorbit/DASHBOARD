<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="tableReviewSummary">
		<div class="panel panel-default">
			<div class="table-responsive">
				<div class="card sb-card">
					<div class="card-block">
						<h3>Reviews created for <Strong><xsl:value-of select="./@developer"/></Strong> for sprint <Strong><xsl:value-of select="./@sprint"/></Strong></h3>
						<p>This gives a way to access review timelines, see revisions, see files, see comments, apply/reference key word indicators.</p>
					</div>
				</div>
							
				<div class="panel-body">
					<table class="table">
						<thead>
							<tr>
								<th>
									#
								</th>
								<th>
									Create Date
								</th>
								<th>
									Summary
								</th>
								<th>
									Revisions
								</th>
							</tr>
						</thead>
						<tbody>
							<xsl:for-each select="./rowSummary">
								<tr>
									<td><xsl:value-of select="position()"/></td>
									<td><xsl:value-of select="./@date"/></td>
									<td><xsl:value-of select="./@summary"/></td>
									<td><xsl:value-of select="./revisions"/></td>
								</tr>
							</xsl:for-each>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>