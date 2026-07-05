package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Advertisement;
import service.AdvertisementService;

import java.io.IOException;
import java.util.List;

@WebServlet("/advertisements")
public class AdvertisementServlet extends HttpServlet {
    private AdvertisementService advertisementService;
    
    @Override
    public void init() {
        advertisementService = new AdvertisementService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        request.setAttribute("advertisements", advertisements);
        
        List<Advertisement> activeAds = advertisementService.getActiveAdvertisements();
        request.setAttribute("activeAds", activeAds);
        
        request.getRequestDispatcher("/Admin_ADVER/AdAdmin.jsp").forward(request, response);
    }
}