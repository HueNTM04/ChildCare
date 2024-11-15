package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import dal.FeedbackDAO;
import dal.ServiceDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Feedback;
import model.Service;
import model.Users;
import utils.EmailUtil;
import jakarta.mail.MessagingException;

/**
 *
 * @author vuvie
 */
public class FeedbackListServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FeedbackListServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        FeedbackDAO feedbackdao = new FeedbackDAO();
        ServiceDAO servicedao = new ServiceDAO();
        UserDAO userdao = new UserDAO();
        List<Feedback> feedbackList = feedbackdao.getAllFeedbacks();
        List<Service> serviceList = servicedao.getAllServices();
        List<Users> userList = userdao.getAllUsers();
        request.setAttribute("service", serviceList);
        request.setAttribute("feedback", feedbackList);
        request.setAttribute("user", userList);
        request.getRequestDispatcher("/Manager_JSP/manager-feedback-list.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        if ("confirm".equals(action)) {
            int feedbackId = Integer.parseInt(request.getParameter("feedbackId"));
            FeedbackDAO feedbackDao = new FeedbackDAO();
            Feedback feedback = feedbackDao.getFeedbackByID(feedbackId);

            if (feedback != null) {
                // Update feedback status
                feedbackDao.updatefeedbackstatus(feedbackId, true);

                try {
                    // Send confirmation email
                    EmailUtil.sendConfirmationEmail(feedback.getEmail(), feedback.getUserName());
                } catch (MessagingException e) {
                    throw new ServletException("Error sending confirmation email", e);
                }

                // Redirect back to feedback list
                response.sendRedirect(request.getContextPath() + "/manager/feedbacklist");
            }
        } else {
            doGet(request, response);
        }
    }
}