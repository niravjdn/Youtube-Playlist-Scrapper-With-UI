package com.techyappdevelopers.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.techyappdevelopers.scrapper.Scrapper;

/**
 * Servlet implementation class ScrapPlaylist
 */
@WebServlet("/ScrapPlaylist")
public class ScrapPlaylist extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ScrapPlaylist() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String playlistURL = request.getParameter("playlistID");
		System.out.println(playlistURL);
		String realPath = getServletContext().getRealPath("/WEB-INF/lib/Driver/");
		new Scrapper().scrape(playlistURL, realPath);
		String playListNames = "";
		for (String str : Scrapper.playlistNameList) {
			playListNames += str + "\n";
		}
		
		String playListID = "";
		playListID = "\"";
		for (String str : Scrapper.playlistIDList) {
			playListID += str + "\n";
		}
		playListID += "\"";
		
		System.out.println(playListNames);
		System.out.println(playListID);
		request.setAttribute("playListNames", playListNames);
		request.setAttribute("playListID", playListID);
        request.getRequestDispatcher("/result.jsp").forward(request, response);
	}

}
