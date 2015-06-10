package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Button;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JFileChooser;
import javax.swing.JButton;

import functools.FileHandler;
import functools.L;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Label;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class FileBroken {
	File choosedFile;
	private JFrame frame;
	Label label_root_filesLabel;
	JFileChooser fileChooser;
	JLabel lblCount ;
	JTextArea textArea_infoArea;
	JProgressBar progressBar ;
	private JMenuBar menuBar_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileBroken window = new FileBroken();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FileBroken() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("Files Choosed");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				frame.getContentPane().add(fileChooser);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				JButton btnFilesChoose = new JButton("FIles Choose");
				

			    int status = fileChooser.showOpenDialog(null);
				if (status == JFileChooser.APPROVE_OPTION) {
					choosedFile = fileChooser.getSelectedFile();
					System.out.println(choosedFile.getParent());
					System.out.println(choosedFile.getName());
					label_root_filesLabel.setText(choosedFile.getAbsolutePath());
				} else if (status == JFileChooser.CANCEL_OPTION) {
				      System.out.println("calceled");

				}
			
			}
		});
		menuBar.add(btnNewButton);
		
		label_root_filesLabel = new Label("root files path");
		menuBar.add(label_root_filesLabel);
		
		JButton btnBroken = new JButton("Broken");
		btnBroken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					progressBar.setValue(2);
					FileHandler handler = new FileHandler(label_root_filesLabel.getText());
					handler.getAllFiles();
					progressBar.setValue(20);
					long count  = handler.count();
					handler.display(textArea_infoArea);
					progressBar.setValue(50);
					L.l();
					lblCount.setText(String.valueOf(handler.count())+"/"+String.valueOf(handler.count())+" ");
//					FileHandler.Broken("/Users/darkh/Desktop/test/gradle.properties", 20);
					handler.BrokensAll();
					progressBar.setValue(100);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});
		menuBar.add(btnBroken);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea_infoArea = new JTextArea();
		scrollPane.setViewportView(textArea_infoArea);
		
		menuBar_1 = new JMenuBar();
		frame.getContentPane().add(menuBar_1, BorderLayout.NORTH);
		
		lblCount = new JLabel("count:");
		menuBar_1.add(lblCount);
		
		progressBar = new JProgressBar();
		menuBar_1.add(progressBar);
		
		
		
	}
	
	
	

}
