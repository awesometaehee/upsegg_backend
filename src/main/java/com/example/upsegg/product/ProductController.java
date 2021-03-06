package com.example.upsegg.product;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.upsegg.configuration.ApiConfiguration;
import com.example.upsegg.product.entity.Product;
import com.example.upsegg.product.entity.ProductFile;
import com.example.upsegg.product.repository.ProductFileRepository;
import com.example.upsegg.product.repository.ProductRepository;

@RestController
public class ProductController {
	private ProductRepository productRepo;
	private ProductFileRepository productFileRepo;
	private final Path FILE_PATH = Paths.get("product_file");
	private ProductService service;

	@Autowired
	private ApiConfiguration apiConfig;

	@Autowired
	public ProductController(ProductRepository productRepo, ProductFileRepository productFileRepo,
			ProductService service) {
		this.productRepo = productRepo;
		this.productFileRepo = productFileRepo;
		this.service = service;
	}

	// 상품 전체 조회
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProducts(HttpServletRequest req) {

		List<Product> list = productRepo.findAll();
		for (Product product : list) {
			for (ProductFile file : product.getFiles()) {
				file.setDataUrl(apiConfig.getBasePath() + "/product-files/" + file.getId());
			}
		}
		System.out.println(list);

		return list;
	}

	// 상품 1건 추가
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public Product addProduct(@RequestBody Product product) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		String Today = sdf.format(c1.getTime());
		product.setReDate(Today);

		productRepo.save(product);

		service.sendProduct(product);

		return product;
	}

	// 상품 1건 수정
	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	public Product modiProduct(@PathVariable("id") long id, @RequestBody Product modifiedProduct,
			HttpServletResponse res) {
		Product product = productRepo.findById(id).orElse(null);

		if (product == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		product.setProductName(modifiedProduct.getProductName());
		product.setDescription(modifiedProduct.getDescription());
		product.setStock(modifiedProduct.getStock());
		product.setPrice(modifiedProduct.getPrice());
		product.setCategory(modifiedProduct.getCategory());
		product.setCode(modifiedProduct.getCode());
		product.setReDate(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));

		productRepo.save(product);

		return product;
	}

	// 상품 1건 삭제
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public boolean removeProduct(@PathVariable("id") long id, HttpServletResponse res) {
		Product product = productRepo.findById(id).orElse(null);

		if (product == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		// 하위 테이블 삭제
		List<ProductFile> files = productFileRepo.findByProductId(id);
		for (ProductFile file : files) {
			productFileRepo.delete(file);
		}

		productRepo.deleteById(id);

		return true;
	}

	// {id}인 product에 product-files 목록 조회
	@RequestMapping(value = "/products/{id}/product-files", method = RequestMethod.GET)
	public List<ProductFile> getProductFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (productRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<ProductFile> productFiles = productFileRepo.findByProductId(id);
		System.out.println(productFiles);

		return productFiles;
	}

	// {id}인 상품 파일 1건 추가
	@RequestMapping(value = "/products/{id}/product-files", method = RequestMethod.POST)
	public ProductFile addProductFile(@PathVariable("id") long id, @RequestPart("data") MultipartFile file,
			HttpServletResponse res) throws IOException {

		if (productRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		System.out.println(file.getOriginalFilename());

		// 디렉토리가 없으면 생성
		if (!Files.exists(FILE_PATH)) {
			Files.createDirectories(FILE_PATH);
		}

		// 파일 저장
		FileCopyUtils.copy(file.getBytes(), new File(FILE_PATH.resolve(file.getOriginalFilename()).toString()));

		// 파일 메타 데이터 저장
		ProductFile productFile = ProductFile.builder().productId(id).fileName(file.getOriginalFilename())
				.contentType(file.getContentType()).build();
		System.out.println(productFile);

		productFileRepo.save(productFile);
		return productFile;
	}

	@RequestMapping(value = "/product-files/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getProductFile(@PathVariable("id") long id, HttpServletResponse res)
			throws IOException {
		ProductFile productFile = productFileRepo.findById(id).orElse(null);

		if (productFile == null) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", productFile.getContentType() + ";charset=UTF-8");
		// inline: 뷰어로, attachement: 내려받기
		responseHeaders.set("Content-Disposition",
				"inline; filename=" + URLEncoder.encode(productFile.getFileName(), "UTF-8"));

		return ResponseEntity.ok().headers(responseHeaders)
				.body(Files.readAllBytes(FILE_PATH.resolve(productFile.getFileName())));
	}
}
