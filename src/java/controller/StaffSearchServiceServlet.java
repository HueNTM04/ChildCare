/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.util.List;

import dal.AgeLimitDAO;
import dal.ServiceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AgeLimits;
import model.Service;

/**
 *
 * @author LENOVO
 */
public class StaffSearchServiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String ageLimitIDStr = request.getParameter("ageLimit");
        int ageLimitID = ageLimitIDStr != null && !ageLimitIDStr.isEmpty() ? Integer.parseInt(ageLimitIDStr) : 0;

        ServiceDAO serviceDAO = new ServiceDAO();
        AgeLimitDAO ageLimitDAO = new AgeLimitDAO();
        List<Service> services;

        if (keyword != null && !keyword.isEmpty() && ageLimitID > 0) {
            services = serviceDAO.searchServicesByKeywordAndAgeLimit(keyword, ageLimitID);
        } else if (keyword != null && !keyword.isEmpty()) {
            services = serviceDAO.searchServicesByKeyword(keyword);
        } else if (ageLimitID > 0) {
            services = serviceDAO.searchServicesByAgeLimitID(ageLimitID);
        } else {
            services = serviceDAO.getAllServices();
        }

        List<AgeLimits> ageLimits = ageLimitDAO.getAllAgeLimits();

        request.setAttribute("services", services);
        request.setAttribute("ageLimits", ageLimits);
        request.getRequestDispatcher("/Staff_JSP/staff-services-list.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}