/**
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.server.rpc.runner;

import java.util.Locale;

import com.greenpepper.runner.SpecificationRunner;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.rpc.runner.report.Report;
import com.greenpepper.server.rpc.runner.report.ReportGenerator;

public class RemoteDocumentRunner
		implements SpecificationRunner
{

	private XmlRpcRemoteRunner xmlRpcRemoteRunner;
	private SpecificationRunnerMonitor monitor;
	private String project;
	private String systemUnderTest;
	private String repositoryId;
	private ReportGenerator reportGenerator;
	private Locale locale;

	public void setXmlRpcRemoteRunner(XmlRpcRemoteRunner xmlRpcRemoteRunner)
	{
		this.xmlRpcRemoteRunner = xmlRpcRemoteRunner;
	}

	public void setMonitor(SpecificationRunnerMonitor monitor)
	{
		this.monitor = monitor;
	}

	public void setProject(String project)
	{
		this.project = project;
	}

	public void setSystemUnderTest(String systemUnderTest)
	{
		this.systemUnderTest = systemUnderTest;
	}

	public void setRepositoryId(String repositoryId)
	{
		this.repositoryId = repositoryId;
	}

	public void setReportGenerator(ReportGenerator reportGenerator)
	{
		this.reportGenerator = reportGenerator;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public void run(String source, String output)
	{
		Report report = null;

		try
		{
			monitor.testRunning(getLocation(source, '/'));

			Execution execution = xmlRpcRemoteRunner.runSpecification(
					project, systemUnderTest, repositoryId, source, false, locale.getLanguage());

			report = reportGenerator.openReport(getLocation(source, '-'));
			
			report.generate(execution);

			monitor.testDone(execution.getSuccess(), execution.getFailures(), execution.getErrors(),
							 execution.getIgnored());
		}
		catch (Throwable t)
		{
			if (report != null)
			{
				report.renderException(t);
			}
			monitor.exceptionOccured(t);
		}
		finally
		{
			closeReport(report);
		}
	}

	private String getLocation(String specificationName, char separator)
	{
		return repositoryId + separator + specificationName;
	}

	private void closeReport(Report report)
	{
		if (report == null)
		{
			return;
		}
		try
		{
			reportGenerator.closeReport(report);
		}
		catch (Exception e)
		{
			monitor.exceptionOccured(e);
		}
	}
}