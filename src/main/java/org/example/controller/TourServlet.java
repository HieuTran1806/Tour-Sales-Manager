package org.example.controller;

import org.example.bus.TourBUS;
import org.example.dto.TourDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/Tour"})
public class TourServlet extends HttpServlet {

    private TourBUS tourBUS;

    @Override
    public void init() throws ServletException {
        super.init();
        tourBUS = new TourBUS();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        ArrayList<TourDTO> ls;

        if ("search".equals(action)) {
            String searchType = request.getParameter("searchType");
            String keyword = request.getParameter("keyword");

            if (keyword != null && !keyword.trim().isEmpty()) {
                switch (searchType) {
                    case "ten":
                        ls = tourBUS.getListsByName(keyword);
                        break;
                    default:
                        ls = tourBUS.getAllTours();
                }
            } else {
                ls = tourBUS.getAllTours();
            }
        } else {
            ls = tourBUS.getAllTours();
        }

        request.setAttribute("danhSachTour", ls);
        request.getRequestDispatcher("/Tour.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String maTour = request.getParameter("maTour");
        String ten = request.getParameter("ten");
        int soNgay = Integer.parseInt(request.getParameter("soNgay"));
        long donGia = Long.parseLong(request.getParameter("donGia"));
        int soCho = Integer.parseInt(request.getParameter("soCho"));
        String ddKhoiHanh = request.getParameter("diaDiemKhoiHanh");
        String imgLink = request.getParameter("imgLink");
        String maLoaiTour = request.getParameter("maLoaiTour");
        String maDiaDiem = request.getParameter("maDiaDiem");

        TourDTO t = new TourDTO(maTour, ten , soNgay, donGia,soCho, ddKhoiHanh, imgLink,maLoaiTour,maDiaDiem);

        if ("add".equals(action)) {
            tourBUS.addTour(t);

        } else if ("delete".equals(action)) {
            String ma = request.getParameter("maTour");
            tourBUS.removeTour(ma);
        } else if ("edit".equals(action)) {
            tourBUS.editTour(t);
        }

        response.sendRedirect(request.getContextPath() + "/Tour");
    }
}