package com.example.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.CreateOrderActivity;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Adapter.ConfirmOrderAdapter;
import com.example.myapplication.Database.DbOrderHelper;
import com.example.myapplication.Database.DbOrderProductHelper;
import com.example.myapplication.Database.DbProductHelper;
import com.example.myapplication.OrderProduct;
import com.example.myapplication.Product;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrderConfirm extends Fragment {
    private Button btnBack, btnConfirm;      //nút trở lại, nút xác nhận
    private String id_Order = "";         // string lưu id của order
    private TextView textViewID, textViewDate, textViewSKUs, textViewSumAmount, textViewSumPrice; //textview hiển thị id, ngày tạo, tổng sku,... của order
    private CreateOrderActivity createOrderActivity;     //activity chứa fragment này
    private DbOrderHelper dbOrderHelper;                 //db chứa thông tin order
    private DbProductHelper dbProductHelper;           //db chứa thông tin product
    private DbOrderProductHelper dbOrderProductHelper;  //db chứa thông tin giao của order và product (product nào thuộc order nào)
    private RecyclerView recyclerView;                  //recyclerview hiển thị những product
    private List<String> orderInfo = new ArrayList<>(); //list chứa thông tin của order, phần tử [0] chứa ngày tạo, [1] chứa tên người đặt hàng, [2] chứa sđt, [3] chứa địa chỉ người đặt, [4] chứa ghi chú của người đặt
    private ConfirmOrderAdapter confirmOrderAdapter;    //adapter của danh sách product thuộc đơn hàng (order)
    private List<Product> productList;          //list chứa danh sách product
    private EditText edtCustomer, edtPhoneNumber, edtAddress, edtNote;  //edittext cho người dùng nhập thông tin
    private String strCustomer = "", strPhoneNumber = "", strAddress = "", strNote = "";   //string chứa thông tin người dùng nhập

    public FragmentOrderConfirm() {
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        // khởi tạo view
        View view = inflater.inflate(R.layout.fragment_order_confirm, container, false);

        // Mở database
        dbOrderHelper = new DbOrderHelper(getActivity());
        dbOrderHelper.getWritableDatabase();
        dbProductHelper = new DbProductHelper(getActivity());
        dbProductHelper.getWritableDatabase();
        dbOrderProductHelper = new DbOrderProductHelper(getActivity());
        dbOrderProductHelper.getWritableDatabase();

        // Lấy ID của đơn hàng thông qua hàm get của activity chứa fragment này
        createOrderActivity = (CreateOrderActivity) getActivity();
        id_Order = createOrderActivity.getOrderId();
        orderInfo = dbOrderHelper.getOrderInfoFromDB(id_Order);

        // Ánh xạ view
        textViewID = (TextView) view.findViewById(R.id.order_confirm_id);
        textViewDate = (TextView) view.findViewById(R.id.order_confirm_date);
        textViewSKUs = (TextView) view.findViewById(R.id.order_confirm_sku);
        textViewSumAmount = (TextView) view.findViewById(R.id.order_confirm_amount);
        textViewSumPrice = (TextView) view.findViewById(R.id.order_confirm_sum_price);
        edtCustomer = (EditText) view.findViewById(R.id.order_customer);
        edtPhoneNumber = (EditText) view.findViewById(R.id.order_phone_number);
        edtAddress = (EditText) view.findViewById(R.id.order_address);
        edtNote = (EditText) view.findViewById(R.id.order_note);

        // khai báo recyclerview và adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_confirm_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        confirmOrderAdapter = new ConfirmOrderAdapter(getActivity());

        // lấy list product và gán vào productList (list này sẽ truyền vào adapter)
        if (getAllProductOrder(id_Order).size() == 0)
            productList = new ArrayList<>();
        else {
            productList = getAllProductOrder(id_Order);
            //set text hiển thị các thông tin của order
            textViewSKUs.setText(productList.size() + "");
            textViewSumAmount.setText(getOrderSumAmount(productList) + "");
            textViewSumPrice.setText(getOrderSumPrice(productList) + "");
        }
        //truyền productList vào adapter và hiển thị lên recyclerview
        confirmOrderAdapter.setData(productList);
        recyclerView.setAdapter(confirmOrderAdapter);

        btnBack = (Button) view.findViewById(R.id.back_btn_confirm);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu người dùng chưa nhấn xác nhận mà nhấn trở lại thì sẽ xóa đơn hàng vừa tạo để tránh lưu thừa thải, tốn bộ nhớ
                dbOrderHelper.deleteOrder(id_Order);
                dbOrderProductHelper.deleteAllFromDB(id_Order);
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        // lấy các text mà người dùng nhập
        edtCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strCustomer = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strPhoneNumber = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strAddress = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strNote = s.toString().trim();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnConfirm = (Button) view.findViewById(R.id.confirm_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu chưa có mặt hàng nào mà người dùng đã nhấn xác nhận thì thông báo như dưới
                if(productList.size() == 0)
                    Toast.makeText(getActivity(), "Chưa có mặt hàng nào!", Toast.LENGTH_SHORT).show();
                //nếu chưa điền đầy đủ thông tin mà đã nhấn xác nhận thì thông báo như bên dưới
                else if (strCustomer.equals("") || strCustomer.isEmpty() || strPhoneNumber.equals("") || strPhoneNumber.isEmpty() ||
                        strAddress.equals("") || strAddress.isEmpty() || strNote.equals("") || strNote.isEmpty()) {
                    Toast.makeText(getActivity(), "Điền đầy đủ thông tin trước khi đặt hàng!" , Toast.LENGTH_SHORT).show();
                }
                //nếu đã điền đầy đủ và nhấn xác nhận thì lưu đống thông tin người dùng vừa nhập (tên, sđt, địa chỉ, ghi chú) vào database với id của order tương ứng
                else {
                    dbOrderHelper.updateCustomerInfo(id_Order, strCustomer, strPhoneNumber, strAddress, strNote);
                    Toast.makeText(getActivity(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

        textViewID.setText(id_Order);
        if (orderInfo.size() != 0) {
            textViewDate.setText(orderInfo.get(0));
        }
        return view;
    }

    //hàm lấy list product thuôc order hiện tại
    public List<Product> getAllProductOrder(String id_order) {
        // -  Lấy danh sách các cột thuộc order hiện tại (mở Database inspector để xem)
        // -  OrderProduct là đối tượng giao giữa Product và Order, thể hiện order nào chứa những product nào,
        // OrderProduct chứa thông tin gồm: ID order, ID product, số lượng mỗi product, ví dụ đơn hàng có ID là
        // DH01 chứa các product là tôm (10 con), cá (3 con), trứng (10 quả),...,
        List<OrderProduct> orderProducts = dbOrderProductHelper.getAllOrderProductFromDB(id_order);

        // từ id những product trong list ở trên, mở database chứa thông tin product, lấy ra product đó với toàn bộ thông tin tương ứng của product, lưu vào 1 list (bên dưới)
        List<Product> products = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product product = dbProductHelper.getProductFromDB(orderProduct.getId_product());
            //set số lượng cho product (số lượng lấy từ thuộc tính amount của list OrderProduct trên)
            product.setAmount(orderProduct.getAmount());
            products.add(product);
        }
        return products;
    }

    // tính tổng số lượng product trong đơn hàng
    public int getOrderSumAmount(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount();
        return s;
    }

    // tính tổng tiền đơn hàng
    public int getOrderSumPrice(List<Product> productLists) {
        int s = 0;
        for (Product product : productLists)
            s += product.getAmount() * product.getPrice();
        return s;
    }
}
