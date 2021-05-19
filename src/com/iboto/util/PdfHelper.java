package com.iboto.util;

import java.io.FileOutputStream;

import com.iboto.models.UserBean;
import com.iboto.models.UserVoteSummary;
import com.iboto.models.VotedCityElectionCandidates;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfHelper {
	
	
	public static void generateVoteSummary(String filePath, UserVoteSummary voteSummary, VotedCityElectionCandidates votedCandidates, UserBean user) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();
			Paragraph p = new Paragraph(voteSummary.getElectionName(), new Font(FontFamily.TIMES_ROMAN, 24f, Font.BOLD));
			p.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(p);
			document.add(generateVoteUserInfo(voteSummary, user));
			document.add(new Paragraph("Voted Candidates"));
			document.add(generateVotedCandidates(votedCandidates));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static PdfPCell emptyCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setPaddingBottom(5f);
		cell.setHorizontalAlignment(alignment);
		return cell;
	}
	
	private static PdfPCell emptyCell(String text) {
		return emptyCell(text, Paragraph.ALIGN_LEFT);
	}
	
	private static PdfPTable generateVoteUserInfo(UserVoteSummary voteSummary, UserBean user) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidths(new float[] {2f, 3f, 1f, 1f});
		table.setWidthPercentage(100f);
		table.setSpacingBefore(25f);
		table.addCell(emptyCell("Reference Number: "));
		table.addCell(emptyCell(voteSummary.getReferenceNumber()));
		table.addCell(emptyCell("Date Voted: "));
		table.addCell(emptyCell(voteSummary.getDateVoted().toString()));
		table.completeRow();
		table.addCell(emptyCell("Voter: "));
		table.addCell(emptyCell(user.getLastName() + ", " + user.getFirstName()));
		table.addCell(emptyCell(""));
		table.addCell(emptyCell(""));
		table.completeRow();
		table.addCell(emptyCell("Address: "));
		table.addCell(emptyCell(user.getCityAddress().getProperName()));
		table.addCell(emptyCell("District: "));
		table.addCell(emptyCell(String.valueOf(user.getDistrict())));
		table.completeRow();
		return table;
	}
	private static PdfPTable generateVotedCandidates(VotedCityElectionCandidates candidates) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(25f);
		table.addCell(emptyCell("Mayor: "));
		table.addCell(emptyCell(candidates.getMayorCandidate().getName(), Paragraph.ALIGN_RIGHT));
		table.addCell(emptyCell("Vice Mayor: "));
		table.addCell(emptyCell(candidates.getViceMayorCandidate().getName(), Paragraph.ALIGN_RIGHT));
		table.addCell(emptyCell("Councilors: "));
		table.addCell(emptyCell(candidates.getCouncilorCandidates().get(0).getName(), Paragraph.ALIGN_RIGHT));
		for (int i = 1; i < candidates.getCouncilorCandidates().size(); i++) {
			table.addCell(emptyCell(""));
			table.addCell(emptyCell(candidates.getCouncilorCandidates().get(i).getName(), Paragraph.ALIGN_RIGHT));
		}
		return table;
	}
}
